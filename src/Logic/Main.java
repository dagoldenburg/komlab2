package Logic;

import Handlers.CallListener;
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

    }
}
