package States;

import Audio.AudioReceive;
import Audio.AudioSend;
import Logic.Main;

import java.io.IOException;
import java.util.Scanner;

public class InSessionState extends State {

    private static Thread audioSendThread = new Thread();
    private static Thread audioReceiveThread= new Thread();

    public static void startAudioThreads(){
        Thread audioSendThread = new Thread(new AudioSend());
        audioSendThread.start();
        Thread audioReceiveThread = new Thread(new AudioReceive());
        audioReceiveThread.start();
        System.out.println("You are now in a call, Write x if you want to hang up");
    }

    private void killThreads(){
        audioSendThread.stop();
        audioReceiveThread.stop();
    }

    @Override
    public State ReceivedBye() {
        try {
            System.out.println("got bye sending ok");
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
            System.out.println("sending bye");
            StateHandler.getToPeer().writeBytes("BYE\n");
        } catch (IOException e) {
            System.out.println("Connection broke");
        }
        killThreads();
        return new ClosingState();
    }
}
