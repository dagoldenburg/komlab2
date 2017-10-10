package ListenForCalls;

import Logic.Main;
import States.CalledState;
import States.InSessionState;
import States.StateHandler;
import States.WaitingState;

import java.util.Scanner;

public class InputHandler implements Runnable {


    @Override
    public void run() {
        System.out.println("taking input");
        Scanner s = new Scanner(System.in);
        while(true) {
            String input = s.nextLine();
            if (input.length() > 4)
                if (input.substring(0, 5).equalsIgnoreCase("call ") && Main.stateHandler.getCurrentState() instanceof WaitingState) {
                    StateHandler.ip = input.substring(5);
                    StateHandler.setCalling(true);
                }
            if (input.length() == 1) {
                System.out.println("1");
                if (input.equalsIgnoreCase("x") && Main.stateHandler.getCurrentState() instanceof InSessionState) {
                    StateHandler.setBeingCalled(false);
                }
                if (input.equalsIgnoreCase("y") && Main.stateHandler.getCurrentState() instanceof CalledState) {
                    CalledState.setYorN('y');
                }
                if (input.equalsIgnoreCase("n") && Main.stateHandler.getCurrentState() instanceof CalledState) {
                    CalledState.setYorN('n');
                }
            }
        }
    }
}
