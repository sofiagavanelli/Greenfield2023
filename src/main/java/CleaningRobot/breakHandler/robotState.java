package CleaningRobot.breakHandler;

enum STATE {
    WORKING,
    NEEDING,
    MECHANIC
}

public class robotState {

    private static STATE current = STATE.WORKING;

    private static robotState instance;

    //singleton
    public static synchronized robotState getInstance(){
        if(instance==null)
            instance = new robotState();
        return instance;
    }

}