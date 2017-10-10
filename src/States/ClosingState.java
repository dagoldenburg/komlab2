package States;

import Logic.Main;

import java.io.IOException;

public class ClosingState extends State  {


    @Override
    public void stateRun() {
        try {
            System.out.println("horan");
            if (StateHandler.getFromPeer().readLine().contains("OK")) {
                Main.stateHandler.invokeReceivedOK();
            }
        } catch (IOException e) {
            Main.stateHandler.invokeReceivedOK();
        }

    }

    @Override
    public State ReceivedOK() {
        return new WaitingState();
    }

}
