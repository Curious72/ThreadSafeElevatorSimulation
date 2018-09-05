package impl;

import interfaces.IElevator;
import interfaces.IMovementBehaviour;

import java.util.*;

public class Elevator implements IElevator ,Runnable {


    public int getId() {
        return id;
    }

    private final int id;
    private Set<Person> people;
    private Map<Floor, ArrayList<Person>> floorToPeopleMap;
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
        this.floorToPeopleMap = new HashMap<>();
        this.people = new HashSet<>();
    }




    public void addPersonToElevator(Person person)
    {
        this.people.add(person);
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

        synchronized (this) {
            if (level != this.currentFloor.getLevel()) {

                if (this.selectedFloors.size() == 0) {
                    if (this.currentFloor.getLevel() > level) {
                        this.setMovementBehaviour(DownwardMovement.instance);
                    } else {
                        this.setMovementBehaviour(UpwardMovement.instance);
                    }

                    this.selectedFloors.add(Building.floors.get(level));

                } else {

                    this.selectedFloors.add(Building.floors.get(level));
                }
            }
        }


    }

    public void selectFloor(Person person, int level)
    {
        Floor floor = Building.floors.get(level);
        synchronized (this) {
            if (!this.floorToPeopleMap.containsKey(floor)) {
                this.floorToPeopleMap.put(floor, new ArrayList<>());
            }

            this.floorToPeopleMap.get(floor).add(person);
        }
        selectFloor(level);
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
        return this.people.size();
    }

    @Override
    public void move() {
        System.out.println("Thread:"+Thread.currentThread()+" | "+" MSG:"+" Current Floor:"+this.currentFloor.getLevel()+" Selected Floors:"+this.selectedFloors.toString()+" Movement Behaviour:"+this.movementBehaviour.toString());

        this.currentFloor = this.movementBehaviour.getNextFloorFromSelected(this.selectedFloors, this.currentFloor);
        this.selectedFloors.remove(this.currentFloor);

        System.out.println("Thread:"+Thread.currentThread()+" | "+" MSG:"+" Current Floor:"+this.currentFloor.getLevel()+" Selected Floors:"+this.selectedFloors.toString()+" Movement Behaviour:"+this.movementBehaviour.toString());

        for(Person person: this.floorToPeopleMap.get(this.currentFloor))
        {
            this.people.remove(person);
        }
        this.floorToPeopleMap.remove(this.currentFloor);

        if(this.selectedFloors.size() == 0)
        {
            this.setMovementBehaviour(NoMovement.instance);
        }

         if( this.currentFloor == Building.floors.get(0)|| this.currentFloor == Building.floors.get(Building.numOfFloors-1) )
        {
            switchMovementBehaviour();
        }

    }

    @Override
    public void run() {
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
