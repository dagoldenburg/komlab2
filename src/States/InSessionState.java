package States;

import Audio.AudioReceive;
import Audio.AudioSend;
import Logic.Main;

import java.io.IOException;

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
    public void stateRun() {
        Runnable byeListener = () -> {
            try{
                if (StateHandler.getFromPeer().readLine().contains("BYE")) {
                    System.out.println("Other end hung up");
                    StateHandler.setBeingCalled(false);
                }
            } catch (IOException e) {
                System.out.println("Connection broke");
                StateHandler.setBeingCalled(false);
            }
        };
        byeThread = new Thread(byeListener);
        byeThread.start();

        Thread audioSendThread = new Thread(new AudioSend());
        audioSendThread.start();
        Thread audioReceiveThread = new Thread(new AudioReceive());
        audioReceiveThread.start();
        System.out.println("You are now in a call, Write x if you want to hang up");
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
            StateHandler.getToPeer().writeBytes("OK\n");
        } catch (IOException e) {
        }
        killThreads();
        return new WaitingState();
    }

}
