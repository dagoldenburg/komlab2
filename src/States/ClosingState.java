package States;

import Logic.Main;

import java.io.IOException;

public class ClosingState extends State  {


    @Override
    public void stateRun() {
        try {
            if (StateHandler.getFromPeer().readLine().contains("OK")) {
                Main.stateHandler.invokeReceivedOK();
            }
        } catch (IOException e) {
            System.out.println("Connection broke");
            Main.stateHandler.invokeReceivedOK();
        }

    }

    @Override
    public State ReceivedOK() {
        return new WaitingState();
    }

}
