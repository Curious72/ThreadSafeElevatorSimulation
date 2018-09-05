package interfaces;

public interface IController {
    void register(IElevator controllable);
    void unregister(IElevator controllable);
    IElevator requestElevator(IMovementBehaviour movementBehaviour, int level);
}
