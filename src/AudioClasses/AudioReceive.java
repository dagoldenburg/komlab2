package AudioClasses;

import ListenForCalls.CallListener;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;

public class AudioReceive extends Audio implements Runnable{
    SourceDataLine speakers;
    String ip;
    DataLine.Info info;
    byte[] receiveBuffer;
    public AudioReceive(String ip){
        super();
        try {
            this.ip = ip;
            info = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(info);
            speakers.open(format);
            speakers.start();
            receiveBuffer = new byte[1024];
        }catch (LineUnavailableException e2) {
            e2.printStackTrace();
        }
    }

    private void send() throws IOException{
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        udpSocket.receive(receivePacket);
    }

    @Override
    public void run() {
        try {
            while (CallListener.getStatus().equals("BUSY")) {
                send();
            }
        }catch(IOException e){
            return;
        }finally{
            speakers.close();
            udpSocket.close();
        }
    }
}
