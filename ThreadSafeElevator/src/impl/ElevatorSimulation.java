package impl;

import interfaces.IElevator;

import java.util.ArrayList;

public class ElevatorSimulation {

    public static void main(String args[])
    {
        int numOfFloors = 10;

        for (int i = 0; i< numOfFloors; i++)
        {
            Building.addFloor(new Floor(i));
        }

        int numOfElevators = 4;
        int totalElevatorCapacity = 5;

        for(int i = 1;i <= numOfElevators; i++)
        {
            IElevator elevator = new Elevator(totalElevatorCapacity, Building.floors.get(0), i);
            Building.addElevator(elevator);
            Controller.instance.register(elevator);
        }



        ArrayList<Person> listOfPersons = new ArrayList<>();
        listOfPersons.add(new Person(0 , UpwardMovement.instance));
        listOfPersons.add(new Person(5, DownwardMovement.instance));
//        listOfPersons.add(new Person(8, UpwardMovement.instance));
//        listOfPersons.add(new Person(5, UpwardMovement.instance));

        for(Person person: listOfPersons)
        {
          Thread t = new Thread(new Runnable() {
              @Override
              public void run() {
                  try {
                      person.requestElevator();
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          });
          t.start();
        }


    }


}
