package Handlers;

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
            Main.stateHandler.makeNewConnection(connection);

        try {
            String input = StateHandler.getFromPeer().readLine();
            if(input.contains("INVITE")){
                Main.stateHandler.invokeReceivedInvite();
            }else if(input.contains("ACK")){
                Main.stateHandler.invokeSendACK();
            }else if(input.contains("BYE")){
                Main.stateHandler.invokeReceivedBye();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }
}
