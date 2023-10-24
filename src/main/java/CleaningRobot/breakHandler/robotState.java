package CleaningRobot.breakHandler;

import AdminServer.beans.RobotInfo;

import java.util.ArrayList;
import java.util.List;

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

    public synchronized STATE getState() {
        return current;
    }

    public synchronized void setState(STATE newS) {
        current = newS;
    }

}