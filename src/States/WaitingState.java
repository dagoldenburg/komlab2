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




    public State ReceivedInvite(){
        Main.setInput(null);
        System.out.println("You are being called, press Y to accept and N to decline");
        Scanner s = new Scanner(System.in);
        try {
            while(true){
                //TODO: timeout
                String input = s.nextLine();
                System.out.println(input);
                if(input.equalsIgnoreCase("y")){
                    StateHandler.getToPeer().writeBytes("TRO\n");
                    return new CalledState();
                }else if(input.equalsIgnoreCase("n")){
                    StateHandler.getToPeer().writeBytes("BUSY\n");
                    return new WaitingState();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
