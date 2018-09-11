package impl;

import interfaces.ElevatorListener;
import interfaces.IElevator;

import java.util.ArrayList;
import java.util.UUID;

public class AsyncResponse {

    private final String id = UUID.randomUUID().toString();

    public AsyncResponse(){
        this.listeners = new ArrayList<>();
    }

    private ArrayList<ElevatorListener> listeners;

    public void addListener(ElevatorListener listener)
    {
        this.listeners.add(listener);
    }

    public void notifyListeners(IElevator elevator){

        for(ElevatorListener listener: listeners)
        {
            listener.invoke(elevator);
        }
    }

}
