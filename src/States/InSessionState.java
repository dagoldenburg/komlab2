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
            StateHandler.toPeer.writeBytes("BYE");
        } catch (IOException e) {
            killThreads();
            return new WaitingState();
        }
        killThreads();
        return new ClosingState();
    }

    @Override
    public void stateRun() {
        Scanner s = new Scanner(System.in);
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                if( s.equals("") )
                {

                }
            }
        };
        Timer timer = new Timer();
        Thread audioSendThread = new Thread(new AudioSend());
        audioSendThread.start();
        Thread audioReceiveThread = new Thread(new AudioReceive());
        audioReceiveThread.start();
        System.out.println("Write x if you want to hang up");
        while(true){
            timer.schedule( task, 1000 );
            if(s.nextLine().equalsIgnoreCase("x")){
                Main.stateHandler.invokeSendBye();
            }
            if(StateHandler.beingCalled==false){
                Main.stateHandler.invokeReceivedBye();
            }
            timer.cancel();
        }
    }

    @Override
    public State ReceivedBye(){
        try {
            StateHandler.toPeer.writeBytes("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        killThreads();
        return new WaitingState();
    }

}
