package States;

import Logic.Main;

import java.io.IOException;

public class CallingState extends State {

    @Override
    public void stateRun() {
        String input = null;
        try {
            input = StateHandler.fromPeer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (input.contains("TRO")) {
            Main.stateHandler.invokeReceivedTRO();
        } else if (input.contains("BUSY")) {
            Main.stateHandler.invokeReceivedBusy();
        }
    }

    @Override
    public State ReceivedTRO() {
        try {
            System.out.println("You are now ni a call");
            StateHandler.toPeer.writeBytes("ACK\n");
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
