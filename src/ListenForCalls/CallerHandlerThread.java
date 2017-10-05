package ListenForCalls;

import AudioClasses.AudioReceive;
import AudioClasses.AudioSend;
import Logic.StateHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class CallerHandlerThread implements Runnable {

    Socket connection;

    CallerHandlerThread(Socket connection){
        this.connection = connection;
    }

    @Override
    public void run() {
        Thread audioReceiveThread = new Thread();
        Thread audioSendThread = new Thread();
        Scanner s = new Scanner(System.in);
            try {
                BufferedReader fromPeer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream toPeer = new DataOutputStream(connection.getOutputStream());
                if(StateHandler.isInSession() || StateHandler.isCalling()){
                    toPeer.writeBytes("BUSY\n");
                    return;
                }
                if(fromPeer.readLine().contains("INVITE")){
                    StateHandler.setStateCalling();
                    String input = s.nextLine();
                    System.out.println("You are being called, do you want to accept? Y/N");
                    if(input.equalsIgnoreCase("N")){
                        return;
                    }
                    toPeer.writeBytes("TRO\n");
                    if(fromPeer.readLine().contains("ACK")){
                        StateHandler.setStateInSession();
                        System.out.println("Press x if you want to hang up.");
                        String ip = connection.getInetAddress().getHostAddress();
                        audioReceiveThread = new Thread(new AudioReceive(ip));
                        audioReceiveThread.start();
                        audioSendThread = new Thread(new AudioSend(ip));
                        audioSendThread.start();
                        while(StateHandler.isInSession()){
                            if(fromPeer.readLine().contains("BYE")){
                                toPeer.writeBytes("ACK");
                            }
                        }
                    }return;
                }return;
            }catch(IOException e){
                e.printStackTrace();
            }finally{ // closing
                try {
                    System.out.println("ended");
                    connection.close();
                    audioReceiveThread.interrupt();
                    audioSendThread.interrupt();
                    StateHandler.setStateWaiting();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return;
    }

}
