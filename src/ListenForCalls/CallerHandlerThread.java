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
        if(Main.stateHandler.getCurrentState() instanceof WaitingState){
            Main.stateHandler.makeNewConnection(connection);
            StateHandler.setBeingCalled(true);
        }
        while(StateHandler.getSocket()!=null) {
            if (StateHandler.isBeingCalled() && StateHandler.isCalling()) {
                while (true) {
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
        }
        return;
    }
}
