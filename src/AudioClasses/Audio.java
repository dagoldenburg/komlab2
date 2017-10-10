package AudioClasses;

import Logic.Ports;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Audio {
    static DatagramSocket udpSocket;
    AudioFormat format;
    DataLine.Info info;

    public Audio(){
        format = new AudioFormat(8000.0f, 16, 1, true, true);
        try {
            if(udpSocket==null) {
                System.out.println("make datagaermsocket");
                udpSocket = new DatagramSocket(Ports.UDP_LISTEN);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
