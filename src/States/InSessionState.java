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


    @Override
    public State SendBye() {
        killThreads();
        try {
            StateHandler.getToPeer().writeBytes("BYE\n");
        } catch (IOException e) {
            killThreads();
            return new WaitingState();
        }
        return new ClosingState();
    }
    @Override
    public State ReceivedACK() {
        Thread audioSendThread = new Thread(new AudioSend());
        audioSendThread.start();
        Thread audioReceiveThread = new Thread(new AudioReceive());
        audioReceiveThread.start();
        System.out.println("You are now in a call, Write x if you want to hang up");
        while(Main.stateHandler.getCurrentState() instanceof InSessionState){
            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            if(input.equals("x")){
                break;
            }
        }
        return new ClosingState();
    }



    @Override
    public State ReceivedBye(){
        try {
            StateHandler.getToPeer().writeBytes("OK\n");
        } catch (IOException e) {
        }
        killThreads();
        return new WaitingState();
    }

}
