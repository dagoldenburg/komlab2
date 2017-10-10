package States;

import Logic.Main;
import Logic.Ports;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class WaitingState extends  State {

    public void stateRun(){
        try {
            Main.stateHandler.removeConnection();
        }catch(NullPointerException e){

        }
        StateHandler.setBeingCalled(false);
        StateHandler.setCalling(false);
        System.out.println("Welcome, if you want to call someone write: call <ip>");
        while(true){
            if(StateHandler.isBeingCalled()){
                Main.stateHandler.invokeReceivedInvite();
            }
            if(StateHandler.isCalling()) {
                try {
                    Main.stateHandler.makeNewConnection(new Socket(InetAddress.getByName(StateHandler.ip), Ports.TCP_SEND));
                    try {
                        if (Main.getFaultyMode()) {
                            System.out.println("faulty mode");
                            StateHandler.getToPeer().writeBytes("FAULTY\n");
                        } else
                            StateHandler.getToPeer().writeBytes("INVITE\n");
                        Main.stateHandler.invokeSendInvite();
                    } catch (IOException e) {
                        System.out.println("Connection broke");
                        Main.stateHandler.invokeReceivedBusy();
                    }
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection timed out");
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("string index oobs");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Unable to find connection to this address");
                }
            }
        }
    }

    public State ReceivedInvite(){
        return new CalledState();
    }

    @Override
    public State SendInvite(){
        return new CallingState();
    }

}
