package States;

import Logic.Main;

import java.io.IOException;

public class ClosingState extends State  {

    @Override
    public State ReceivedBye() {
        try {
            StateHandler.getToPeer().writeBytes("OK");
        } catch (IOException e) {
            System.out.println("Connection broke");
        }
        return new WaitingState();
    }
}