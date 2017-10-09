package States;

import Logic.Main;
import Logic.Ports;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class WaitingState extends  State {


    Scanner userInput;

    public WaitingState() {

        if(Main.stateHandler == null){
            System.out.println("ass");
        }

        try {
            Main.stateHandler.removeConnection();
        }catch(NullPointerException e){

        }
        userInput = new Scanner(System.in);
        String input;
        String ip;
        while(true){
            System.out.println("Welcome, if you want to call someone write: call <ip>");
            input = userInput.nextLine();
            try {
                if (input.substring(0, 5).equalsIgnoreCase("call ")) {
                    ip = input.substring(5);
                    Main.stateHandler.makeNewConnection(new Socket(InetAddress.getByName(ip), Ports.TCP_SEND));
                    try{
                        if (Main.getFaultyMode()) {
                            System.out.println("faulty mode");
                            StateHandler.toPeer.writeBytes("FAULTY\n");
                        }else
                            StateHandler.toPeer.writeBytes("INVITE\n");
                            Main.stateHandler.invokeSendInvite();
                    }catch(IOException e){

                    }
                }
            } catch(SocketTimeoutException e){
                System.out.println("Connection timed out");
            } catch(StringIndexOutOfBoundsException e){

            }catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to find connection to this address");
            }
        }
    }

    public State ReceivedInvite(){
        System.out.println("invite");
        return new CalledState();
    }

    @Override
    public State SendInvite(){
        return new CallingState();
    }

}
