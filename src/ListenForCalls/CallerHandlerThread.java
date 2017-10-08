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
        }
        Main.stateHandler.invokeReceivedInvite();
        while(StateHandler.socket!=null){
            try {
                if(StateHandler.fromPeer.readLine().contains("BYE")){
                    Main.stateHandler.invokeReceivedBye();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }
}
