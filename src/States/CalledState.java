package States;

import Audio.AudioReceive;
import Audio.AudioSend;
import Logic.Main;

import java.io.IOException;
import java.util.Scanner;

public class CalledState extends State {

    @Override
    public State ReceivedACK(){
        return new InSessionState();
    }

}
