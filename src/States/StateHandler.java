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
    public static String ip;
    public static int port;

    public synchronized static BufferedReader getFromPeer() {
        return fromPeer;
    }

    public synchronized static DataOutputStream getToPeer() {
        return toPeer;
    }

    public synchronized static Socket getSocket() {
        return socket;
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
        try {
            socket.close();
            toPeer.close();
            fromPeer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public State getCurrentState(){
        return currentState;
    }

    public void initStates(){
        currentState = new WaitingState();
    }

    public void invokeReceivedInvite(){currentState = currentState.ReceivedInvite();}
    public void invokeSendInvite(){currentState = currentState.SendInvite();}
    public void invokeSendTRO(){currentState = currentState.SendTRO();}
    public void invokeSendACK(){currentState = currentState.SendACK();}
    public void invokeSendBye(){currentState = currentState.SendBye();}
    public void invokeReceivedTRO(){currentState = currentState.ReceivedTRO();}
    public void invokeReceivedOK(){currentState = currentState.ReceivedOK();}
    public void invokeReceivedBye(){currentState = currentState.ReceivedBye();}
    public void invokeReceivedBusy(){currentState = currentState.ReceivedBusy();}
    public void invokeReceivedACK(){currentState = currentState.ReceivedACK();}
}
