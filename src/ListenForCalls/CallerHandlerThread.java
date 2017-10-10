package ListenForCalls;

import Logic.Main;
import States.StateHandler;
import States.WaitingState;

import java.io.DataOutputStream;
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
        }else{
            try {
                DataOutputStream toPeer = new DataOutputStream(connection.getOutputStream());
                toPeer.writeBytes("BUSY\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }
}
