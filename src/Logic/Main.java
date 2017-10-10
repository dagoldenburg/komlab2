package Logic;

import ListenForCalls.CallListener;
import ListenForCalls.InputHandler;
import States.StateHandler;

import java.io.IOException;

public class Main {
    public static StateHandler stateHandler;
    static boolean faultyMode;

    public static boolean getFaultyMode(){
        return faultyMode;
    }

    public static void main(String[] args){
        if(args.length>0)
            faultyMode = true;
        Thread t = null;
        try {
            t = new Thread(new CallListener());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        t.start();
        t = new Thread(new InputHandler());
        t.start();
        stateHandler = new StateHandler();
        stateHandler.initStates();
    }
}
