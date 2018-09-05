package impl;

import interfaces.IController;
import interfaces.IElevator;
import interfaces.IMovementBehaviour;

import java.util.ArrayList;

public class Controller implements IController {

    public final static Controller instance = new Controller();

    private Controller() {
        this.elevators = new ArrayList<>();
    }

    private ArrayList<IElevator> elevators;

    @Override
    public void register(IElevator elevator) {
        elevators.add(elevator);
    }

    @Override
    public void unregister(IElevator controllable) {
        elevators.remove(controllable);
    }

    @Override
    public IElevator requestElevator(IMovementBehaviour movementBehaviour, int level) {

        for (IElevator elevator : elevators) {
            IMovementBehaviour elevatorMovementBehaviour = elevator.getMovementBehaviour();
            if (elevatorMovementBehaviour == NoMovement.instance) {
                elevator.selectFloor(level);
                return elevator;
            } else if (elevatorMovementBehaviour == movementBehaviour) {
                Floor floor = elevator.getCurrentFloor();
                if (Math.abs(floor.getLevel() - level) > Math.abs(elevatorMovementBehaviour.getNextFloor(floor).getLevel() - level)) {
                    elevator.selectFloor(level);
                    return elevator;
                }
            }

        }

        return elevators.get(0);

    }
}
