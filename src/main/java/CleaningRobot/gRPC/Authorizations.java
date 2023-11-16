package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import com.example.chat.CommunicationServiceOuterClass.*;

import java.util.*;
import java.util.logging.Logger;

public class Authorizations {

    List<Authorization> authorizations;
    
    private static Authorizations instance;

    private static final Logger logger = Logger.getLogger(Authorizations.class.getSimpleName());

    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %3$s : %5$s %n");
    }

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
        authorizations = new ArrayList<>();
    }

    public synchronized List<Authorization> getAuthorizations() {
        return authorizations;
    }

    public boolean isPresent(int id) {

        if(authorizations.get(id).getOk())
            return true;
        else
            return false;

    }

    public void removeOne(int id) {

        List<Authorization> listCopy = getAuthorizations();

        for(Authorization a : listCopy) {
            if (a.getFrom() == (id)) {
                synchronized (authorizations) {
                    authorizations.removeIf(req -> req.getFrom() == id);
                }
            }
        }
    }

    public void controlAuthorizations() {

        synchronized(authorizations) {
            //funzione chiamata dopo una richiesta del meccanico
            while (authorizations.size() < (RobotList.getInstance().getRobotslist().size() - 1)) {
                logger.info("I'm waiting for some authorizations");
                try {
                    authorizations.wait();
                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
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
