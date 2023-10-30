package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.breakHandler.STATE;
import CleaningRobot.breakHandler.robotState;
import com.example.chat.CommunicationServiceOuterClass.*;
//import sun.misc.Queue;

import java.util.*;

public class Authorizations {

    List<Authorization> authorizations;

    private static Authorizations instance;

    private Authorizations() {
        authorizations = new ArrayList<Authorization>(); //ne crea una nuova//
    }

    //singleton
    public static synchronized Authorizations getInstance(){
        if(instance==null)
            instance = new Authorizations();
        return instance;
    }

    public void addAuthorization(Authorization newA) {
        authorizations.add(newA);
    }

    public void removeAll() {
        //authorizations = null;
        authorizations = new ArrayList<>();
    }

    public List<Authorization> getAuthorizations() {
        return authorizations;
    }

    public synchronized void controlAuthorizations() {

        if(robotState.getInstance().getState() == STATE.NEEDING) { //funzione chiamata dopo una richiesta del meccanico
            while (authorizations.size() <
                    (RobotList.getInstance().getRobotslist().size() - 1)) {
                System.out.println("i'm waiting for some authorizations");
                try {
                    authorizations.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else //questa funzione è stata chiamata per risvegliare dei thread in attesa dopo il rilascio del meccanico
            authorizations.notify();

    }
}
