package States;

import Audio.AudioReceive;
import Audio.AudioSend;
import Logic.Main;

import java.io.IOException;
import java.util.Scanner;

public class InSessionState extends State {

    Thread audioSendThread = new Thread();
    Thread audioReceiveThread= new Thread();
    Thread byeThread = new Thread();

    private void killThreads(){
        audioSendThread.stop();
        audioReceiveThread.stop();
        byeThread.stop();
    }

    private static char char2 = 'i';

    public synchronized static void setChar2(char newChar){
        char2=newChar;
    }

    public synchronized static char getChar2(){
        return char2;
    }


    @Override
    public State ReceivedACK() {
        setChar2('i');
        Thread audioSendThread = new Thread(new AudioSend());
        audioSendThread.start();
        Thread audioReceiveThread = new Thread(new AudioReceive());
        audioReceiveThread.start();
        System.out.println("You are now in a call, Write x if you want to hang up");
        System.out.println(Main.stateHandler.getCurrentState());
        while(Main.stateHandler.getCurrentState() instanceof InSessionState){
            if(getChar2()=='x'){
                try {
                    StateHandler.getToPeer().writeBytes("BYE\n");
                } catch (IOException e) {
                    System.out.println("Connection broke");
                }
                break;
            }
            if(getChar2()=='o'){
                try {
                    StateHandler.getToPeer().writeBytes("OK\n");
                } catch (IOException e) {
                    System.out.println("Connection broke;");
                }
                killThreads();
                return new WaitingState();
            }
        }

        return new ClosingState();
    }

}
