package States;

import Logic.Main;

import java.io.IOException;

public class ClosingState extends State  {

    @Override
    public State ReceivedOK() {
        System.out.println("closing state");
        return new WaitingState();
    }
}