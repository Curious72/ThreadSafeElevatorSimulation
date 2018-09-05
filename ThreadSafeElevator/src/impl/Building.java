package impl;

import interfaces.IElevator;

import java.util.ArrayList;

public class Building {
    public static ArrayList<IElevator> elevators = new ArrayList<>();
    public static ArrayList<Floor> floors = new ArrayList<>();
    public static int numOfFloors  = 0;
    public static int numOfElevators = 0;



    public static void addElevator(IElevator elevator)
    {
        elevators.add(elevator);
        numOfElevators = numOfElevators + 1;
    }

    public static void addFloor(Floor floor)
    {
        floors.add(floor);
        numOfFloors = numOfFloors + 1;
    }

}
