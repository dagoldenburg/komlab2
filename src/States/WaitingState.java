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
    Thread t = new Thread();
    public WaitingState(){
        t = new Thread(new Input());
        t.start();
    }

    private class Input implements Runnable{

        @Override
        public void run() {
            String input;
            while (true) {
                if (Main.stateHandler.getCurrentState() instanceof WaitingState) {
                    Scanner s = new Scanner(System.in);
                    System.out.println("Welcome, write \"call <ip>:<port>\" to call someone");
                    input = s.nextLine();
                    try {
                        if (input.substring(0, 5).equalsIgnoreCase("call ")) {
                            String temp = input.substring(5);
                            String[] info = temp.split(":");
                            try {
                                StateHandler.ip = info[0];
                                StateHandler.port = Integer.parseInt(info[1]);
                                Main.stateHandler.invokeSendInvite();
                                Main.stateHandler.invokeReceivedTRO();
                                Main.stateHandler.invokeReceivedACK();
                            } catch (ArrayIndexOutOfBoundsException e) {
                                System.out.println("Need <ip>:<port>, Example: 0.0.0.0:9999");
                            }
                        } else System.out.println("Invalid input");
                    }catch(StringIndexOutOfBoundsException e){

                    }catch(NullPointerException e2){
                        System.out.println("Got nulled");
                    }
                }
            }
        }
    }


    public State ReceivedInvite(){
        t.interrupt();
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
