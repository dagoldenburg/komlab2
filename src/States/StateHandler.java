package States;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class StateHandler {
    private State currentState;
    private static Socket socket = null;
    private static DataOutputStream toPeer = null;
    public static BufferedReader fromPeer = null;
    private static boolean beingCalled = false;
    private static boolean calling = false;
    public static String ip;

    public synchronized static boolean isBeingCalled() {
        return beingCalled;
    }

    public synchronized static void setBeingCalled(boolean beingCalled) {
        StateHandler.beingCalled = beingCalled;
    }

    public synchronized static boolean isCalling() {
        return calling;
    }

    public synchronized static void setCalling(boolean calling) {
        StateHandler.calling = calling;
    }

    public synchronized static BufferedReader getFromPeer() {
        return fromPeer;
    }

    public synchronized static void setFromPeer(BufferedReader fromPeer) {
        StateHandler.fromPeer = fromPeer;
    }

    public synchronized static DataOutputStream getToPeer() {
        return toPeer;
    }

    public synchronized static void setToPeer(DataOutputStream toPeer) {
        StateHandler.toPeer = toPeer;
    }

    public synchronized static Socket getSocket() {
        return socket;
    }

    public synchronized static void setSocket(Socket socket) {
        StateHandler.socket = socket;
    }

    public void makeNewConnection(Socket socket){
        try {
            this.socket = socket;
            toPeer = new DataOutputStream(socket.getOutputStream());
            fromPeer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(socket.getInetAddress().getHostAddress());
        }catch(IOException e){
            e.printStackTrace();
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
