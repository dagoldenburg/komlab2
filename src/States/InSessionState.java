package States;

import Audio.AudioReceive;
import Audio.AudioSend;
import Logic.Main;

import java.io.IOException;
import java.util.Scanner;

public class InSessionState extends State {

    private Thread audioSendThread = new Thread();
    private Thread audioReceiveThread= new Thread();

    private void killThreads(){
        audioSendThread.stop();
        audioReceiveThread.stop();
    }

    public InSessionState(){
        Thread audioSendThread = new Thread(new AudioSend());
        audioSendThread.start();
        Thread audioReceiveThread = new Thread(new AudioReceive());
        audioReceiveThread.start();
        System.out.println("You are now in a call, Write x if you want to hang up");
    }

    @Override
    public State ReceivedBye() {
        try {
            StateHandler.getToPeer().writeBytes("OK\n");
        } catch (IOException e) {
            System.out.println("Connection broke;");
        }
        killThreads();
        return new WaitingState();
    }

    @Override
    public State SendBye() {
        try {
            StateHandler.getToPeer().writeBytes("BYE\n");
        } catch (IOException e) {
            System.out.println("Connection broke");
        }
        killThreads();
        return new ClosingState();
    }
}
