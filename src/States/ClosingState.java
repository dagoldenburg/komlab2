package States;

import Logic.Main;

import java.io.IOException;

public class ClosingState extends State  {

    @Override
    public State ReceivedBye() {
        try {
            if(StateHandler.getFromPeer().readLine().contains("OK"));
        } catch (IOException e) {
            System.out.println("Connection broke");
        }
        return new WaitingState();
    }
}