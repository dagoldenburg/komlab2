package Handlers;

import Logic.Main;
import Logic.Ports;
import States.InSessionState;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class CallListener implements Runnable {

    private ServerSocket serverSocket;
    public CallListener() throws IOException{
        try {
            serverSocket = new ServerSocket(Ports.TCP_LISTEN);
        }catch(BindException e){
            System.out.println("Listen port is busy, shutting down");
            System.exit(0);
        }
    }

    @Override
    public void run(){
        while(true){
            try {
                Socket connectionSocket = serverSocket.accept();

                // if busy() ... slipper starta en tråd
                if(Main.stateHandler.getCurrentState() instanceof InSessionState){
                    try {
                        DataOutputStream toPeer = new DataOutputStream(connectionSocket.getOutputStream());
                        toPeer.writeBytes("BUSY\n");
                    } catch (IOException e) {
                    }
                }

                Thread t = new Thread(new CallerHandlerThread(connectionSocket));
                t.start();
            } catch (IOException e) {
                System.out.println("Cant receive calls anymore, shutting down");
                System.exit(0);
            }
        }
    }


}
