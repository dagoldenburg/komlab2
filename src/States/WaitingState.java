package States;

import Logic.Main;
import Logic.Ports;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class WaitingState extends  State {

    static char char1 = 'i';

    public static synchronized void setChar1(char newChar){
        char1 = newChar;
    }

    public static synchronized char getChar1(){
        return char1;
    }


    public WaitingState(){
        System.out.println("Welcome, write \"call <ip>:<port>\" to call someone");
    }

    public State ReceivedInvite(){
        setChar1('i');
        System.out.println("You are being called, press Y to accept and N to decline");
        try {
            while(true){
                if(getChar1()=='y'){
                    StateHandler.getToPeer().writeBytes("TRO\n");
                    return new CalledState();
                }else if(getChar1()=='n'){
                    StateHandler.getToPeer().writeBytes("BUSY\n");
                    return new WaitingState();
                }
            }
        } catch (IOException e) {
            System.out.println("Connection broke");
        }
        return new WaitingState();
    }

    @Override
    public State SendInvite(){
        Main.stateHandler.removeConnection();
        try {
            Ports.TCP_SEND = StateHandler.port;
            Main.stateHandler.makeNewConnection(new Socket(InetAddress.getByName(StateHandler.ip), Ports.TCP_SEND));
            try {
                if (Main.getFaultyMode()) {
                    System.out.println("faulty mode");
                    StateHandler.getToPeer().writeBytes("FAULTY\n");
                } else {
                    StateHandler.getToPeer().writeBytes("INVITE\n");
                }
                return new CallingState();
            } catch (IOException e) {
                System.out.println("Connection broke");
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Connection timed out");
        }catch (IOException e) {
            System.out.println("Unable to find connection to this address");
        }
        return new WaitingState();
    }
}
