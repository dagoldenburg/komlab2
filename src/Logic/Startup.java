package Logic;

import AudioClasses.AudioReceive;
import AudioClasses.AudioSend;
import ListenForCalls.CallListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Startup {
    private static Socket socket;
    private static BufferedReader fromPeer;
    private static DataOutputStream toPeer;

    public static void main(String[] args){
        String INVITE = "INVITE";
        if(args.length>0){
            System.out.println("faulty mode");
            INVITE = "FAULTY";
        }
        Thread t = null;
        try {
            t = new Thread(new CallListener());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        t.start();

        Scanner s = new Scanner(System.in);
        String input;
        String ip;
        Thread audioSendThread = new Thread();
        Thread audioReceiveThread = new Thread();
        while(true){ //waiting
            System.out.println("Welcome, if you want to call someone write: call <ip>");
            input = s.nextLine();
            try {
                if (input.substring(0, 5).equalsIgnoreCase("call ")) {
                    StateHandler.setStateCalling();
                    ip = input.substring(5);
                    socket = new Socket(InetAddress.getByName(ip), Ports.TCP_SEND);
                    toPeer = new DataOutputStream(socket.getOutputStream());
                    fromPeer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    toPeer.writeBytes(INVITE+"\n");
                    try {
                        if (!fromPeer.readLine().contains("TRO")) {
                            throw new IOException();
                        }
                    }catch(NullPointerException e){
                        throw new StringIndexOutOfBoundsException();
                    }
                    toPeer.writeBytes("ACK\n");
                    StateHandler.setStateInSession();
                    audioSendThread = new Thread(new AudioSend(ip));
                    audioSendThread.start();
                    audioReceiveThread = new Thread(new AudioReceive(ip));
                    audioReceiveThread.start();
                    while(StateHandler.isInSession()){ // in session
                        System.out.println("Press x if you want to hang up.");
                        input = s.nextLine();
                        if(input.equalsIgnoreCase("x")){
                            toPeer.writeBytes("BYE\n");
                        }
                    }
                }
            } catch(SocketTimeoutException e){
                System.out.println("Connection timed out");
            } catch(StringIndexOutOfBoundsException e){
                System.out.println("Faulty input");
            } catch (IOException e) {
                System.out.println("Unable to find connection to this address");
            }finally{
                try {
                    audioSendThread.interrupt();
                    audioReceiveThread.interrupt();
                    try {
                        socket.close();
                    }catch(NullPointerException e3){}
                    StateHandler.setStateWaiting();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
