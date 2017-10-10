package States;

import Logic.Main;

import java.io.IOException;
import java.util.Scanner;

public class CalledState extends State {

    private static char YorN;

    public static synchronized void setYorN(char input){
        YorN=input;
    }
    public static synchronized char getYorN(){
        return YorN;
    }

    CalledState(){
        YorN = 'x';
    }

    @Override
    public void stateRun() {
        System.out.println("You are being called, press Y to accept and N to decline");
        while(true){
            if(getYorN()=='y'){
                Main.stateHandler.invokeSendTRO();
            }else if(getYorN()=='n'){
                Main.stateHandler.invokeSendBusy();
            }
        }
    }

    @Override
    public State SendTRO() {
        try {
            if(StateHandler.getFromPeer().readLine().contains("INVITE")){
                StateHandler.getToPeer().writeBytes("TRO\n");
            }
            if(StateHandler.getFromPeer().readLine().contains("ACK")){
                return new InSessionState();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return new WaitingState();
    }

    @Override
    public State SendBusy(){
        return new WaitingState();
    }

}
