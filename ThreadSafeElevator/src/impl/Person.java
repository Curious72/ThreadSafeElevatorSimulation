package impl;

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
        this.elevator =  Controller.instance.requestElevator(this.movementBehaviour, this.floor.getLevel());
        System.out.println("Thread:"+Thread.currentThread()+" | MSG:"+" Allocated elevator:"+this.elevator.getId());
        int count = 0;
        while(this.elevator.getCurrentFloor() != this.floor)
        {
            Thread.sleep(100);
            count = count + 1;
            if(count == 100)
            {
                break;
            }
        }
        if(this.elevator.getCurrentFloor() == this.floor)
        {
            this.elevator.addPersonToElevator(this);
            Floor requestedFloor;
            if (this.movementBehaviour == UpwardMovement.instance)
                requestedFloor = Building.floors.get((new Random()).nextInt(Building.numOfFloors - this.floor.getLevel()-1) + this.floor.getLevel());
            else
                requestedFloor = Building.floors.get((new Random()).nextInt(this.floor.getLevel()-1));

            this.selectFloor(requestedFloor);
        }
    }
    public void selectFloor(Floor floor){
            this.elevator.selectFloor(this, floor.getLevel());

    }



}
