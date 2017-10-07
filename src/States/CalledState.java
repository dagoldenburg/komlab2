package States;

public class CalledState extends State {
    @Override
    public State ReceivedACK() {
        return new InSessionState();
    }

    @Override
    public State SendTRO() {
        return ReceivedACK();
    }

    @Override
    public State SendBusy() {
        return new WaitingState();
    }
}
