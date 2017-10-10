package ListenForCalls;

import Logic.Main;
import States.StateHandler;
import States.WaitingState;

import java.io.IOException;
import java.net.Socket;

public class CallerHandlerThread implements Runnable {

    Socket connection;

    CallerHandlerThread(Socket connection){
        this.connection = connection;
    }

    @Override
    public void run() {
        System.out.println("received call");
        if(Main.stateHandler.getCurrentState() instanceof WaitingState){
            System.out.println("setup connection");
            Main.stateHandler.makeNewConnection(connection);
            StateHandler.setBeingCalled(true);
        }
        while(StateHandler.getSocket()!=null) {
            System.out.println("seriöst");
            if (StateHandler.isBeingCalled() && StateHandler.isCalling()) {
                try {
                    if (StateHandler.getFromPeer().readLine().contains("BYE")) {
                        System.out.println("Other end hung up");
                        StateHandler.setBeingCalled(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
}
