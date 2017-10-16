package Handlers;

import Logic.Main;
import States.CalledState;
import States.InSessionState;
import States.StateHandler;
import States.WaitingState;

import java.util.Scanner;

public class InputHandler implements Runnable {


    @Override
    public void run() {
        Scanner s = new Scanner(System.in);
        while (true) {
            String input = s.nextLine();
            if (Main.stateHandler.getCurrentState() instanceof WaitingState) {
                System.out.println("Welcome, write \"call <ip>:<port>\" to call someone");
                if (input.substring(0, 5).equalsIgnoreCase("call ")) {
                    String temp = input.substring(5);
                    String[] info = temp.split(":");
                    try {
                        StateHandler.ip = info[0];
                        StateHandler.port = Integer.parseInt(info[1]);
                        Main.stateHandler.invokeSendInvite();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Need <ip>:<port>, Example: 0.0.0.0:9999");
                    }
                } else System.out.println("Invalid input");
            }
        }
    }
}
