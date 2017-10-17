package Handlers;

import Logic.Main;
import States.InSessionState;
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
            while(true) {
                String input = StateHandler.getFromPeer().readLine();
                System.out.println(input);
                if (input.contains("INVITE")) {
                    Main.stateHandler.invokeReceivedInvite();
                } else if (input.contains("ACK")) {
                    Main.stateHandler.invokeReceivedACK();
                } else if (input.contains("BYE")) {
                    Main.stateHandler.invokeReceivedBye();
                }else if(input.contains("OK")){
                    Main.stateHandler.invokeReceivedOK();
                }
            }
        } catch (IOException e) {
            System.out.println("Connection broke");
        }

    }
}
