package States;

public class StateHandler {
    private  State currentState;

    public StateHandler(){
        currentState  =  new WaitingState();
    }
}
