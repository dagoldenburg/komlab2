package States;

import AudioClasses.AudioReceive;
import AudioClasses.AudioSend;
import Logic.Main;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class InSessionState extends State {

    Thread audioSendThread = new Thread();
    Thread audioReceiveThread= new Thread();

    private void killThreads(){
        audioSendThread.stop();
        audioReceiveThread.stop();
    }


    @Override
    public State SendBye() {
        try {
            StateHandler.getToPeer().writeBytes("BYE");
        } catch (IOException e) {
            killThreads();
            return new WaitingState();
        }
        killThreads();
        return new ClosingState();
    }

    @Override
    public void stateRun() {
        Thread audioSendThread = new Thread(new AudioSend());
        audioSendThread.start();
        Thread audioReceiveThread = new Thread(new AudioReceive());
        audioReceiveThread.start();
        System.out.println("Write x if you want to hang up");
        StateHandler.setCalling(true);
        StateHandler.setBeingCalled(true);
        while(true){
            if(!StateHandler.isCalling()){
                Main.stateHandler.invokeSendBye();
            }
            if(!StateHandler.isBeingCalled()){
                Main.stateHandler.invokeReceivedBye();
            }
        }
    }

    @Override
    public State ReceivedBye(){
        try {
            StateHandler.getToPeer().writeBytes("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        killThreads();
        return new WaitingState();
    }

}
