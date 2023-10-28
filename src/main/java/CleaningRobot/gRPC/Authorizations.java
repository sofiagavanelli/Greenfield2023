package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import com.example.chat.CommunicationServiceOuterClass.*;
//import sun.misc.Queue;

import java.util.*;

public class Authorizations {

    List<Authorization> authorizations;

    private static Authorizations instance;

    private Authorizations() {
        authorizations = new ArrayList<Authorization>(); //ne crea una nuova//
        //robotsList.add(new RobotInfo(12, 7));
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

}
