package States;

public class WaitingState extends  State {

    public WaitingState() {

    }

    @Override
    public State ReceivedInvite() {
        return  new CalledState();
    }

    @Override
    public State SendInvite() {
        //skeicka invite

        return new CallingState();
    }
}
