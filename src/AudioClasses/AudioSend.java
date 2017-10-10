package AudioClasses;

import ListenForCalls.CallListener;
import Logic.Main;
import Logic.Ports;
import States.InSessionState;
import States.StateHandler;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;

public class AudioSend extends Audio implements Runnable {
    TargetDataLine microphone;
    DataLine.Info info;
    byte[] sendBuffer;
    InetAddress ip;
//https://stackoverflow.com/questions/2083342/how-to-send-audio-stream-via-udp-in-java

    //https://stackoverflow.com/questions/25798200/java-record-mic-to-byte-array-and-play-sound

    public AudioSend(Socket socket){
        super();
        try {
            ip = socket.getInetAddress();
            info  = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
            sendBuffer = new byte[1024];
        }catch (LineUnavailableException e2) {
            e2.printStackTrace();
        }
    }
    private void send() throws IOException{
            microphone.read(sendBuffer, 0, sendBuffer.length);
            DatagramPacket packet = new DatagramPacket(sendBuffer,
                    sendBuffer.length,
                    ip,
                    Ports.UDP_SEND);
            super.udpSocket.send(packet);
    }
    @Override
    public void run() {
        try {
            while (Main.stateHandler.getCurrentState() instanceof InSessionState) {
                send();
            }
        }catch(IOException e){
            return;
        }finally{
            System.out.println("fin");
            microphone.close();
            try {
                udpSocket.close();
                udpSocket = null;
            }catch(NullPointerException e){

            }
            return;
        }
    }
}
