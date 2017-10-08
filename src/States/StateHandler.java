package States;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class StateHandler {
    private State currentState;
    public static Socket socket;
    public static DataOutputStream toPeer;
    public static BufferedReader fromPeer;

    public void makeNewConnection(Socket socket){
        try {
            socket = socket;
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



    public StateHandler(){
        currentState  =  new WaitingState();
    }

    public void invokeReceivedInvite(){currentState = currentState.ReceivedInvite();}
    public void invokeSendInvite(){currentState = currentState.SendInvite();}
    public void invokeSendTRO(){currentState = currentState.SendTRO();}
    public void invokeSendBusy(){currentState = currentState.SendBusy();}
    public void invokeSendBye(){currentState = currentState.SendBye();}
    public void invokeReceivedTRO(){currentState = currentState.ReceivedTRO();}
    public void invokeReceivedOK(){currentState = currentState.ReceivedOK();}
    public void invokeReceivedBye(){currentState = currentState.ReceivedBye();}
    public void invokeReceivedBusy(){currentState = currentState.ReceivedBusy();}
}
