package States;

public class CallingState extends State {

    @Override
    public State ReceivedTRO() {

        return SendACK();
    }

    @Override
    public State ReceivedBusy() {
        return new WaitingState();
    }

    @Override
    public State SendACK() {
        //send
        return new InSessionState();
    }
}
