package impl;

import interfaces.IController;
import interfaces.IElevator;
import interfaces.IMovementBehaviour;

import java.rmi.activation.ActivationSystem;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Controller implements IController {

    public final static Controller instance = new Controller();
    private ConcurrentHashMap<Integer, ArrayList<AsyncResponse>> floorToResponseMap;

    private Controller() {
        this.elevators = new ArrayList<>();
        this.floorToResponseMap = new ConcurrentHashMap<>();
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
    public AsyncResponse requestElevator(IMovementBehaviour movementBehaviour, int level) {
        System.out.println("Thread Name: "+Thread.currentThread());
        AsyncResponse response = new AsyncResponse();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread Name: "+Thread.currentThread()+" Finding relevant elevator for request at level: "+level);
                for (IElevator elevator : elevators) {
                    IMovementBehaviour elevatorMovementBehaviour = elevator.getMovementBehaviour();
                    if (elevatorMovementBehaviour == NoMovement.instance) {
                        elevator.selectFloor(level);
                        break;
                    } else if (elevatorMovementBehaviour == movementBehaviour) {
                        Floor floor = elevator.getCurrentFloor();
                        if (Math.abs(floor.getLevel() - level) > Math.abs(elevatorMovementBehaviour.getNextFloor(floor).getLevel() - level)) {
                            elevator.selectFloor(level);
                            break;
                        }
                    }

                }


            }
        });


        if(this.floorToResponseMap.containsKey(level))
        {
            this.floorToResponseMap.get(level).add(response);
        }
        else
        {
            ArrayList temp = new ArrayList();
            temp.add(response);
            this.floorToResponseMap.put(level, temp);
        }
        t.start();
        return response;
    }

    @Override
    public void notifyListeners(IElevator elevator, int level)
    {
        System.out.println("Thread Name: "+Thread.currentThread()+" Notifying listeners");
        if(this.floorToResponseMap.containsKey(level))
        {

            for (AsyncResponse response: this.floorToResponseMap.get(level))
            {
                response.notifyListeners(elevator);
            }
        }

    }

    @Override
    public AsyncResponse requestFloor(IElevator elevator, int level) {
//        System.out.println("Thread Name: "+Thread.currentThread()+" Requesting floor: "+level);
        AsyncResponse response = new AsyncResponse();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread Name: "+Thread.currentThread()+" Requesting floor: "+level);
              elevator.selectFloor(level);
            }
        });

        if(this.floorToResponseMap.containsKey(level))
        {
            this.floorToResponseMap.get(level).add(response);
        }
        else
        {
            ArrayList temp = new ArrayList();
            temp.add(response);
            this.floorToResponseMap.put(level, temp);
        }
        t.start();
        return response;

    }
}
