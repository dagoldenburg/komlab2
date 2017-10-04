package Logic;

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
                    socket.setSoTimeout(10000);
                    toPeer.writeBytes("INVITE");
                    if(!fromPeer.readLine().equals("TRO")){
                        throw new IOException();
                    }
                    toPeer.writeBytes("ACK");
                    StateHandler.setStateInSession();
                    while(StateHandler.isInSession()){ // in session
                        System.out.println("Press x if you want to hang up.");
                        audioSendThread = new Thread(new AudioSend(ip));
                        audioSendThread.start();
                        input = s.nextLine();
                        if(input.equalsIgnoreCase("x")){
                            toPeer.writeBytes("BYE");
                            StateHandler.setStateClosing();
                        }
                    }
                }
            } catch(SocketTimeoutException e){
                System.out.println("Connection timed out");
            } catch(StringIndexOutOfBoundsException e){
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    StateHandler.setStateClosing();
                    audioSendThread.interrupt();
                    socket.close();
                    StateHandler.setStateWaiting();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
