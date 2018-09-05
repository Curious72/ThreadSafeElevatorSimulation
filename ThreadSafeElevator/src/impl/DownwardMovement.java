package impl;

import interfaces.IMovementBehaviour;

import java.util.TreeSet;

public class DownwardMovement implements IMovementBehaviour {

    public final static IMovementBehaviour instance = new DownwardMovement();

    private DownwardMovement(){}

    public IMovementBehaviour getOppositeMovementBehaviour(){
        return UpwardMovement.instance;
    }

    @Override
    public Floor getNextFloor(Floor currentFloor) {
        if (currentFloor.getLevel() == 0)
        {
            return currentFloor;
        }
        return Building.floors.get(currentFloor.getLevel()-1);
    }

    @Override
    public Floor getNextFloorFromSelected(TreeSet<Floor> selectedFloors, Floor currentFloor) {
        return selectedFloors.lower(currentFloor);
    }
}
