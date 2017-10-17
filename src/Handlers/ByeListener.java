package Handlers;

import Logic.Main;
import States.InSessionState;
import States.StateHandler;

import java.io.IOException;

public class ByeListener implements Runnable {
    @Override
    public void run() {
        try {
            if(StateHandler.fromPeer.readLine().contains("BYE")){
                Main.stateHandler.invokeReceivedBye();
            }
        } catch (IOException e) {
            System.out.println("Connection broke");
        }
    }
}
