package impl;

import interfaces.IMovementBehaviour;

import java.util.TreeSet;

public class NoMovement implements IMovementBehaviour {

    public final static IMovementBehaviour  instance = new NoMovement();

    private NoMovement(){}

    @Override
    public Floor getNextFloorFromSelected(TreeSet<Floor> selectedFloors, Floor currentFloor) {
        return currentFloor;
    }

    @Override
    public IMovementBehaviour getOppositeMovementBehaviour() {
        return instance;
    }

    @Override
    public Floor getNextFloor(Floor currentFloor) {
        return currentFloor;
    }
}
