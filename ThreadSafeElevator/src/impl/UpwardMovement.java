package impl;

import interfaces.IMovementBehaviour;

import java.util.TreeSet;

public class UpwardMovement implements IMovementBehaviour {

    public final static IMovementBehaviour instance = new UpwardMovement();
    private UpwardMovement() {}

    public IMovementBehaviour getOppositeMovementBehaviour(){
        return DownwardMovement.instance;
    }

    @Override
    public Floor getNextFloor(Floor currentFloor) {
        if (currentFloor.getLevel() == Building.numOfFloors-1)
        {
            return currentFloor;
        }
        return Building.floors.get(currentFloor.getLevel()+1);
    }

    @Override
    public Floor getNextFloorFromSelected(TreeSet<Floor> selectedFloors, Floor currentFloor) {
        return selectedFloors.higher(currentFloor);
    }

}
