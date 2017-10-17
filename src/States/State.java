package States;

public abstract class State {

    public State ReceivedInvite(){return new WaitingState();}
    public State SendInvite(){return new WaitingState();}
    public State ReceivedACK(){return  new WaitingState();}
    public State SendACK(){return new WaitingState();}
    public State ReceivedTRO(){return  new WaitingState();}
    public State SendTRO(){return new WaitingState();}
    public State ReceivedBusy(){return  new WaitingState();}
    public State SendBusy(){return new WaitingState();}
    public State ReceivedOK(){return  new WaitingState();}
    public State SendOK(){return new WaitingState();}
    public State ReceivedBye(){return new WaitingState();}
    public State SendBye(){return new WaitingState();}
    public State ResetState(){return new WaitingState();}


}
