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

    Object lock = new Object();

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

    public synchronized void addAuthorization(Authorization newA) {
        authorizations.add(newA);
    }

    public synchronized void removeAll() {
        //authorizations = null;
        authorizations = new ArrayList<>();
    }

    public synchronized List<Authorization> getAuthorizations() {
        return authorizations;
    }

    /*
    public void controlAuthorizations() {

        //togliere
        synchronized(lock) {
            //if(robotState.getInstance().getState() == STATE.NEEDING) {
            //funzione chiamata dopo una richiesta del meccanico
            while (authorizations.size() < (RobotList.getInstance().getRobotslist().size() - 1)) {
                System.out.println("i'm waiting for some authorizations");
                try {
                    lock.wait();
                } catch (IntegererruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
         //questa funzione è stata chiamata per risvegliare dei thread in attesa dopo il rilascio del meccanico

    }

    public void unblockMechanic() {

        synchronized(lock) {
            if (Authorizations.getInstance().getAuthorizations().size()
                    == (RobotList.getInstance().getRobotslist().size() - 1))
                lock.notifyAll();
        }
    }*/


    public void controlAuthorizations() {

        //togliere
        synchronized(authorizations) {
            //if(robotState.getInstance().getState() == STATE.NEEDING) {
            //funzione chiamata dopo una richiesta del meccanico
            while (authorizations.size() < (RobotList.getInstance().getRobotslist().size() - 1)) {
                System.out.println("i'm waiting for some authorizations");
                try {
                    authorizations.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //questa funzione è stata chiamata per risvegliare dei thread in attesa dopo il rilascio del meccanico

    }

    public void unblockMechanic() {

        synchronized(authorizations) {
            if (Authorizations.getInstance().getAuthorizations().size()
                    == (RobotList.getInstance().getRobotslist().size() - 1))
                authorizations.notifyAll();
        }

    }


}
