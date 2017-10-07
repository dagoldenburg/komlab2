package States;

public class InSessionState extends State {

    @Override
    public State ReceiveBye() {
        return SendOK();
    }

    @Override
    public State SendOK() {
        return new WaitingState();
    }

    @Override
    public State SendBye() {
        return new ClosingState();
    }

}
