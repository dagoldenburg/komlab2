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
        System.out.println(Main.stateHandler.getCurrentState());
        if(Main.stateHandler.getCurrentState() instanceof WaitingState){
            System.out.println("setup connection");
            Main.stateHandler.makeNewConnection(connection);
            StateHandler.setBeingCalled(true);
        }
        while(StateHandler.socket!=null){
            try {
                if(StateHandler.fromPeer.readLine().contains("BYE")){
                    System.out.println("Other end hung up");
                    StateHandler.setBeingCalled(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }
}
