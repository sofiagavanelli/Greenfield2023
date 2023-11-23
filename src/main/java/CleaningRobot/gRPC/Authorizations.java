package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import com.example.chat.CommunicationServiceOuterClass.*;

import java.util.*;
import java.util.logging.Logger;

public class Authorizations {

    final List<Authorization> authorizations;
    
    private static Authorizations instance;

    private static final Logger logger = Logger.getLogger(Authorizations.class.getSimpleName());

    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %3$s : %5$s %n");
    }

    private Authorizations() {
        authorizations = new ArrayList<Authorization>();
    }

    //singleton
    public static synchronized Authorizations getInstance(){
        if(instance==null)
            instance = new Authorizations();
        return instance;
    }

    public void addAuthorization(Authorization newA) {
        synchronized(authorizations) {
            authorizations.add(newA);
            logger.info("i have " + authorizations.size() + " auth of " + (RobotList.getInstance().getRobotslist().size()-1));
            if(authorizations.size() == (RobotList.getInstance().getRobotslist().size()-1))
                authorizations.notifyAll();
        }
    }

    public void removeAll() {
        synchronized(authorizations) {
            authorizations.clear();
        }
    }

    public List<Authorization> getAuthorizations() {
        synchronized(authorizations) {
            return authorizations;
        }
    }

    //mettere una copia non è ok ma mettere i synchronized rischia di metterci troppo per acquisire il lock!!
    public boolean isPresent(int from) {
        //i use a copy
        //List<Authorization> copy = getAuthorizations();
        //return copy.get(from).getOk();
        synchronized (authorizations) {
            //return authorizations.get(from).getOk();
            for(Authorization a : authorizations) {
                if(a.getFrom() == from)
                    return true;
            }
            return false;
        }
    }

    public void removeOne(int botPort) {

        List<Authorization> listCopy = getAuthorizations();

        for(Authorization a : listCopy) {
            if (a.getFrom() == (botPort)) {
                synchronized (authorizations) {
                    authorizations.removeIf(req -> req.getFrom() == botPort);
                }
            }
        }
    }

    //what IF i'm waiting for some authorization and the robot using it has crashed? and nobody needs the mechanic
    //for a while? i'll be waiting for SOME TIME
    public void controlAuthorizations() {

        synchronized(authorizations) {
            //funzione chiamata dopo una richiesta del meccanico
            while (authorizations.size() < (RobotList.getInstance().getRobotslist().size() - 1)) {
                logger.info("I'm waiting for some authorizations");
                try {
                    authorizations.wait();
                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
                    logger.warning("Problems with authorization's wait");
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
