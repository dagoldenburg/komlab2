package Audio;

import Logic.Main;
import States.InSessionState;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class AudioReceive extends Audio implements Runnable{
    SourceDataLine speakers;
    byte[] receiveBuffer;
    public AudioReceive() {
        super();
        try {
            info = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(info);
            speakers.open(format);
            speakers.start();
            receiveBuffer = new byte[1024];
        } catch (LineUnavailableException e2) {
            System.out.println("No speaker device");
        }
    }

    private void send() throws IOException{
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        try {
            udpSocket.receive(receivePacket);
        }catch(SocketException e){

        }
        speakers.write(receiveBuffer, 0, receiveBuffer.length);
    }

    @Override
    public void run() {
        try {
            while (true) {
                send();
            }
        }catch(IOException e){
            e.printStackTrace();
            return;
        }finally{
            speakers.close();try {
                udpSocket.close();
                udpSocket = null;
            }catch(NullPointerException e){

            }
            return;
        }
    }
}
