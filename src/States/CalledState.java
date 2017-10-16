package States;

import Audio.AudioReceive;
import Audio.AudioSend;
import Logic.Main;

import java.io.IOException;
import java.util.Scanner;

public class CalledState extends State {

    @Override
    public State SendACK() {
        try {
            StateHandler.getToPeer().writeBytes("ACK\n");
            return new InSessionState();
        } catch (IOException e) {
            System.out.println("Connection broke");
        }
        return new WaitingState();
    }

}
