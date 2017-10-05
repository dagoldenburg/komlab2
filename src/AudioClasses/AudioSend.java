package AudioClasses;

import ListenForCalls.CallListener;
import Logic.Ports;
import Logic.StateHandler;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class AudioSend extends Audio implements Runnable {
    TargetDataLine microphone;
    DataLine.Info info;
    String ip;
    byte[] sendBuffer;
//https://stackoverflow.com/questions/2083342/how-to-send-audio-stream-via-udp-in-java

    //https://stackoverflow.com/questions/25798200/java-record-mic-to-byte-array-and-play-sound

    public AudioSend(String ip){
        super();
        try {
            this.ip = ip;
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
        //System.out.println("send");
            microphone.read(sendBuffer, 0, sendBuffer.length);
            DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(ip), Ports.UDP_SEND);
            super.udpSocket.send(packet);
    }
    @Override
    public void run() {
        try {
            while (StateHandler.isInSession()) {
                send();
            }
        }catch(IOException e){
            e.printStackTrace();
            return;
        }finally{
            microphone.close();
            udpSocket.close();
        }
    }
}
