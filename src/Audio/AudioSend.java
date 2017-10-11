package Audio;

import Logic.Main;
import Logic.Ports;
import States.InSessionState;
import States.StateHandler;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;

public class AudioSend extends Audio implements Runnable {
    TargetDataLine microphone;
    byte[] sendBuffer;

    public AudioSend(){
        super();
        try {
            info  = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
            sendBuffer = new byte[1024];
        }catch (LineUnavailableException e2) {
            System.out.println("No microphone device");
        }
    }
    private void send() throws IOException{
            microphone.read(sendBuffer, 0, sendBuffer.length);
            DatagramPacket packet = new DatagramPacket(sendBuffer,
                    sendBuffer.length,
                    StateHandler.getSocket().getInetAddress(),
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
