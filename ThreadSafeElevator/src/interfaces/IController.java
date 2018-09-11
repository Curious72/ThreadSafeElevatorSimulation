package interfaces;

import impl.AsyncResponse;

public interface IController {
    void register(IElevator elevator);
    void unregister(IElevator elevator);
    AsyncResponse requestElevator(IMovementBehaviour movementBehaviour, int level);
    void notifyListeners(IElevator elevator, int level);
    AsyncResponse requestFloor(IElevator elevator, int level);
}
