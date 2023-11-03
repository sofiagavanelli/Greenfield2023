package CleaningRobot.breakHandler;

import AdminServer.beans.RobotInfo;
import CleaningRobot.personalInfo;

import java.util.ArrayList;
import java.util.List;

public class robotState {

    public static STATE current = STATE.WORKING;

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
        personalInfo.getInstance().incrementClock();
    }

}