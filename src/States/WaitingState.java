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


    Scanner userInput;
    String input = new String();


    public void stateRun(){
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                if( input.equals("") )
                {

                }
            }
        };
        try {
            Main.stateHandler.removeConnection();
        }catch(NullPointerException e){

        }
        userInput = new Scanner(System.in);
        String ip;
        System.out.println("Welcome, if you want to call someone write: call <ip>");
        while(true){
            Timer timer = new Timer();
            timer.schedule( task, 1000 );
            input = userInput.nextLine();
            timer.cancel();
            if(StateHandler.beingCalled == true){
                Main.stateHandler.invokeReceivedInvite();
            }
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
        return new CalledState();
    }

    @Override
    public State SendInvite(){
        return new CallingState();
    }

}
