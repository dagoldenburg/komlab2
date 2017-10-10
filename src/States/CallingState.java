package States;

import Logic.Main;

import java.io.IOException;

public class CallingState extends State {

    @Override
    public void stateRun() {
        String input = null;
        try {
            input = StateHandler.getFromPeer().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (input.contains("TRO")) {
            System.out.println();
            Main.stateHandler.invokeReceivedTRO();
        } else if (input.contains("BUSY")) {
            Main.stateHandler.invokeReceivedBusy();
        }
    }

    @Override
    public State ReceivedTRO() {
        try {
            System.out.println("You are now in a call");
            StateHandler.getToPeer().writeBytes("ACK\n");
            return new InSessionState();
        } catch (IOException e) {
            e.printStackTrace();
            return new WaitingState();
        }

    }

    @Override
    public State ReceivedBusy(){
        System.out.println("User is busy");
        return new WaitingState();
    }
}
