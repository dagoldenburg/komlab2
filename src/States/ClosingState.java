package States;

public class ClosingState extends State  {

    @Override
    public State ReceiveBye() {
        return SendOK();
    }

    @Override
    public State SendOK() {
        return new WaitingState();
    }
}
