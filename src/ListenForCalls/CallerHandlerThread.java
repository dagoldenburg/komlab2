package ListenForCalls;

import AudioClasses.AudioReceive;
import AudioClasses.AudioSend;
import Logic.StateHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CallerHandlerThread implements Runnable {

    String status;
    Socket connection;

    CallerHandlerThread(Socket connection){
        this.status = status;
        this.connection = connection;
    }

    @Override
    public void run() {
        Thread audioReceiveThread;
            try {
                BufferedReader fromPeer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream toPeer = new DataOutputStream(connection.getOutputStream());
                if(StateHandler.isInSession()){
                    toPeer.writeBytes("BUSY");
                    return;
                }
                if(fromPeer.readLine().equals("INVITE")){
                    StateHandler.setStateCalling();
                    toPeer.writeBytes("TRO");
                    if(fromPeer.readLine().equals("ACK")){
                        StateHandler.setStateInSession();
                        System.out.println("Press x if you want to hang up.");
                        String ip =connection.getInetAddress().getHostAddress();
                        audioReceiveThread = new Thread(new AudioReceive(ip));
                        audioReceiveThread.start();
                        while(StateHandler.isInSession()){
                            if(fromPeer.readLine().equals("BYE")){
                                toPeer.writeBytes("ACK");
                                StateHandler.setStateClosing();
                            }
                        }
                    }else throw new IOException();
                }else throw new IOException();
            }catch(IOException e){
                e.printStackTrace();
            }finally{ // closing
                try {
                    connection.close();
                    StateHandler.setStateWaiting();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return;
    }

}
