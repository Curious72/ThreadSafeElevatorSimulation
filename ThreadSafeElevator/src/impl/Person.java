package impl;

import interfaces.ElevatorListener;
import interfaces.IElevator;
import interfaces.IMovementBehaviour;

import java.util.Random;

public class Person {

    private Floor floor;
    private IMovementBehaviour movementBehaviour;
    private IElevator elevator;

    public Person(int level, IMovementBehaviour movementBehaviour)
    {
        this.floor = Building.floors.get(level);
        this.movementBehaviour = movementBehaviour;
    }

    public void requestElevator() throws InterruptedException {
        Floor requestedFloor;
        if (this.movementBehaviour == UpwardMovement.instance)
            requestedFloor = Building.floors.get((new Random()).nextInt(Building.numOfFloors - this.floor.getLevel()-1) + this.floor.getLevel());
        else
            requestedFloor = Building.floors.get((new Random()).nextInt(this.floor.getLevel()-1));

        System.out.println("Thread Name: "+Thread.currentThread()+" Current floor of person: "+ this.floor.getLevel());

        Person self = this;
        Controller.instance.requestElevator(this.movementBehaviour, this.floor.getLevel()).addListener(new ElevatorListener() {
            @Override
            public void invoke(IElevator elevator) {
                elevator.boardElevator(self);
                System.out.println("Thread Name :"+Thread.currentThread()+" | MSG:"+" Allocated elevator:"+elevator.getId()+" Floor requested by Person:"+ requestedFloor);
                Controller.instance.requestFloor(elevator, requestedFloor.getLevel()).addListener(new ElevatorListener() {
                    @Override
                    public void invoke(IElevator elevator) {
                        self.floor = elevator.getCurrentFloor();
                        elevator.leaveElevator(self);
                        System.out.println("Thread Name: "+Thread.currentThread()+" Reached floor: "+ self.floor);

                    }
                });

            }
        });

    }

}
