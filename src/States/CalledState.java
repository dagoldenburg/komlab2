package States;

import Logic.Main;

import java.io.IOException;
import java.util.Scanner;

public class CalledState extends State {

    CalledState(){
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.println("You are being called, press Y to accept and N to decline");
            if(s.nextLine().equalsIgnoreCase("y")){
                Main.stateHandler.invokeSendTRO();
            }else if(s.nextLine().equalsIgnoreCase("n")){
                Main.stateHandler.invokeSendBusy();
            }
        }
    }

    @Override
    public State SendTRO() {
        try {
            StateHandler.toPeer.writeBytes("TRO\n");
            if(StateHandler.fromPeer.readLine().contains("ACK")){
                return new InSessionState();
            }
        }catch(IOException e){

        }
        return new WaitingState();
    }

    @Override
    public State SendBusy(){
        return new WaitingState();
    }

}
