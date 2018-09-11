package interfaces;
import impl.Floor;
import impl.Person;

public interface IElevator {
     int getId();
     IMovementBehaviour getMovementBehaviour();
     int getCurrentCapacity();
     void selectFloor(int level);
//     void selectFloor(Person person, int level);
     void move();
     void setMovementBehaviour(IMovementBehaviour movementBehaviour);
     Floor getCurrentFloor();
     void boardElevator(Person person);
     void leaveElevator(Person person);

}
