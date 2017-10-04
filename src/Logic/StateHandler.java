package Logic;

public class StateHandler {

    private static String state = "WAITING";

    public String getState(){
        return state;
    }

    public static boolean isInSession(){
        if(state.equals("INSESSION")){
            return true;
        }
    }

    public static void setStateCalling(){
        state = "CALLING";
    }
    public static void setStateClosing(){
        state = "CLOSING";
    }

    public static void setStateInSession(){
        state = "INSESSION";
    }

    public static void setStateWaiting(){
        state = "WAITING";
    }

}
