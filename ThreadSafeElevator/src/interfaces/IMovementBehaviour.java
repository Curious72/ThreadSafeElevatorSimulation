package interfaces;
import impl.Floor;

import java.util.TreeSet;

public interface IMovementBehaviour {
    Floor getNextFloorFromSelected(TreeSet<Floor> selectedFloors, Floor currentFloor);
    IMovementBehaviour getOppositeMovementBehaviour();
    Floor getNextFloor(Floor currentFloor);

}
