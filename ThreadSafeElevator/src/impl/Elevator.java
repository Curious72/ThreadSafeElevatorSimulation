package impl;

import interfaces.IElevator;
import interfaces.IMovementBehaviour;

import java.util.*;

public class Elevator implements IElevator {


    private int currentCapacity;

    public int getId() {
        return id;
    }

    private final int id;
    private Set<Person> people;
    private TreeSet<Floor> selectedFloors;
    private IMovementBehaviour movementBehaviour;
    private Floor currentFloor;
    public final int totalCapacity ;

    public Elevator(int totalCapacity, Floor currentFloor, int id) {
        this.id = id;
        this.totalCapacity = totalCapacity;
        this.currentFloor = currentFloor;
        this.movementBehaviour = NoMovement.instance;
        this.selectedFloors = new TreeSet<>();
        this.people = new HashSet<>();
        this.currentCapacity = 0;
    }




    public void boardElevator(Person person)
    {
        this.currentCapacity += 1;
        this.people.add(person);
    }

    public void leaveElevator(Person person)
    {
     this.currentCapacity -= 1;
     this.people.remove(person);
    }

    public void setMovementBehaviour(IMovementBehaviour movementBehaviour)
    {
        this.movementBehaviour = movementBehaviour;
    }

    @Override
    public Floor getCurrentFloor() {
        return this.currentFloor;
    }

    @Override
    public void selectFloor(int level) {

        System.out.println("Select floor called for level: "+level+" and for elevator: "+this.id+" current floor is : "+this.getCurrentFloor().getLevel());
        synchronized (this) {

            if (level != this.currentFloor.getLevel()) {

                if (this.selectedFloors.size() == 0) {
                    if (this.currentFloor.getLevel() > level) {
                        this.setMovementBehaviour(DownwardMovement.instance);
                    } else {
                        this.setMovementBehaviour(UpwardMovement.instance);
                    }

                    this.selectedFloors.add(Building.floors.get(level));
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runElevator();
                        }
                    });
                    t.start();

                } else {

                    this.selectedFloors.add(Building.floors.get(level));
                }
            }
            else
            {
                Controller.instance.notifyListeners(this, level);
            }
        }


    }

    @Override
    public IMovementBehaviour getMovementBehaviour() {
        return this.movementBehaviour;
    }

    public void switchMovementBehaviour(){
        this.movementBehaviour =  this.movementBehaviour.getOppositeMovementBehaviour();
    }

    @Override
    public int getCurrentCapacity() {
        return this.currentCapacity;
    }

    @Override
    public void move() {

        this.currentFloor = this.movementBehaviour.getNextFloorFromSelected(this.selectedFloors, this.currentFloor);
        System.out.println("Thread:"+Thread.currentThread()+" | "+" MSG:"+" Current Floor:"+this.currentFloor.getLevel()+" Selected Floors:"+this.selectedFloors.toString()+" Movement Behaviour:"+this.movementBehaviour.toString());
        Controller.instance.notifyListeners(this, this.currentFloor.getLevel());
        this.selectedFloors.remove(this.currentFloor);

        if(this.selectedFloors.size() == 0)
        {
            this.setMovementBehaviour(NoMovement.instance);
        }

         if( this.currentFloor == Building.floors.get(0)|| this.currentFloor == Building.floors.get(Building.numOfFloors-1) )
        {
            switchMovementBehaviour();
        }

    }

    public void runElevator() {
        System.out.println("Run was Called");
        while(this.selectedFloors.size() != 0)
        {
            move();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
