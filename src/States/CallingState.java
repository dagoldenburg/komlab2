package States;

import Handlers.ByeListener;
import Logic.Main;

import java.io.IOException;
import java.net.SocketException;

public class CallingState extends State {


    @Override
    public State ReceivedTRO() {
        String input = null;
        try {
            input = StateHandler.getFromPeer().readLine();
            if (input.contains("TRO")) {
                StateHandler.getToPeer().writeBytes("ACK\n");
                Thread t = new Thread(new ByeListener());
                t.start();
                InSessionState.startAudioThreads();
                return new InSessionState();
            }else if(input.contains("BUSY")){
                return new WaitingState();
            }
        } catch (IOException e) {
            System.out.println("Connection broke");
        }
        return new WaitingState();
    }

}
