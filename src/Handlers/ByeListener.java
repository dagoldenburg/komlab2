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
                InSessionState.setChar2('o');
            }
        } catch (IOException e) {
        }
    }
}
