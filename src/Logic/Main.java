package Logic;

import Handlers.CallListener;
import States.InSessionState;
import States.StateHandler;
import States.WaitingState;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static StateHandler stateHandler;
    static boolean faultyMode;

    public static boolean getFaultyMode(){
        return faultyMode;
    }

    public static void main(String[] args){
        Ports.TCP_LISTEN = Integer.parseInt(args[0]);
        if(args.length>1)
            faultyMode = true;
        Thread t = null;
        try {
            t = new Thread(new CallListener());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        t.start();
        stateHandler = new StateHandler();
        stateHandler.initStates();
        String input;
        Scanner s = new Scanner(System.in);
        while (true) {
                if(Main.stateHandler.getCurrentState() instanceof WaitingState) {
                    System.out.println("Welcome, write \"call <ip>:<port>\" to call someone");
                    input = s.nextLine();
                try{
                    if (input.substring(0, 5).equalsIgnoreCase("call ")) {
                        String temp = input.substring(5);
                        String[] info = temp.split(":");
                        try {
                            StateHandler.ip = info[0];
                            StateHandler.port = Integer.parseInt(info[1]);
                            Main.stateHandler.invokeSendInvite();
                            Main.stateHandler.invokeReceivedTRO();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Need <ip>:<port>, Example: 0.0.0.0:9999");
                        }
                    }
                }catch(StringIndexOutOfBoundsException e){
                }
                if(input.equalsIgnoreCase("y")) {
                    WaitingState.setChar1('y');
                }if(input.equalsIgnoreCase("n")){
                    WaitingState.setChar1('n');
                }
                } else if(Main.stateHandler.getCurrentState() instanceof InSessionState) {
                    input = s.nextLine();
                    if (input.equalsIgnoreCase("x")) {
                        Main.stateHandler.invokeSendBye();
                    }
                }
            }
        }
    }

