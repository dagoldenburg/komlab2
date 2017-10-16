package States;

import Logic.Main;

import java.io.IOException;

public class ClosingState extends State  {

    @Override
    public State ReceivedBye() {
        try {
            StateHandler.getToPeer().writeBytes("OK\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new WaitingState();
    }
}