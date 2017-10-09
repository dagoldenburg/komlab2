package States;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class StateHandler {
    private State currentState;
    public static Socket socket = null;
    public static DataOutputStream toPeer = null;
    public static BufferedReader fromPeer = null;
    public static boolean beingCalled = false;

    public void makeNewConnection(Socket socket){
        try {
            this.socket = socket;
            toPeer = new DataOutputStream(socket.getOutputStream());
            fromPeer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){

        }
    }

    public void removeConnection(){
        socket=null;
        toPeer=null;
        fromPeer=null;
    }

    public State getCurrentState(){
        return currentState;
    }

    public void initStates(){
        currentState = new WaitingState();
        currentState.stateRun();
    }

    public void invokeReceivedInvite(){currentState = currentState.ReceivedInvite();currentState.stateRun();}
    public void invokeSendInvite(){currentState = currentState.SendInvite();currentState.stateRun();}
    public void invokeSendTRO(){currentState = currentState.SendTRO();currentState.stateRun();}
    public void invokeSendBusy(){currentState = currentState.SendBusy();currentState.stateRun();}
    public void invokeSendBye(){currentState = currentState.SendBye();currentState.stateRun();}
    public void invokeReceivedTRO(){currentState = currentState.ReceivedTRO();currentState.stateRun();}
    public void invokeReceivedOK(){currentState = currentState.ReceivedOK();currentState.stateRun();}
    public void invokeReceivedBye(){currentState = currentState.ReceivedBye();currentState.stateRun();}
    public void invokeReceivedBusy(){currentState = currentState.ReceivedBusy();currentState.stateRun();}
}
