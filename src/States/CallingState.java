package States;

import Handlers.ByeListener;
import Logic.Main;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CallingState extends State {


    @Override
    public State ReceivedTRO() {
        String input = null;
        try {
            input = StateHandler.getFromPeer().readLine();
            if (input.contains("TRO")) {
                StateHandler.getSocket().setSoTimeout(0);
                StateHandler.getToPeer().writeBytes("ACK\n");
                Thread t = new Thread(new ByeListener());
                t.start();
                return new InSessionState();
            }else if(input.contains("BUSY")){
                StateHandler.getSocket().setSoTimeout(0);
                System.out.println("User is busy");
                return new WaitingState();
            }
        }catch(SocketTimeoutException e){
            System.out.println("Timed out");
            try {
                StateHandler.getToPeer().writeBytes("BYE\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e) {
            System.out.println("Connection broke");
        }
        return new WaitingState();
    }


}
