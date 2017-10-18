package Handlers;

import Logic.Main;
import States.StateHandler;

import java.io.IOException;

public class ByeListener implements Runnable {
    @Override
    public void run() {
        try {
            while(true) {
                    String input = StateHandler.fromPeer.readLine();
                    if (input.contains("BYE")) {
                        Main.stateHandler.invokeReceivedBye();
                        return;
                    } else if (input.contains("OK")) {
                        Main.stateHandler.invokeReceivedOK();
                        return;
                    }
            }
        } catch (IOException e) {
            System.out.println("Connection broke");
            Main.stateHandler.invokeResetState();
        }
    }
}
