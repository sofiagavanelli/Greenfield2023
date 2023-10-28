package CleaningRobot.gRPC;

import CleaningRobot.breakHandler.robotState;
import com.example.chat.CommunicationServiceOuterClass.*;
//import sun.misc.Queue;

import java.util.*;

public class MechanicRequests {

    List<Request> requests;
    Request personal;

    private static MechanicRequests instance;

    private MechanicRequests() {
        requests = new ArrayList<Request>(); //ne crea una nuova//
        //robotsList.add(new RobotInfo(12, 7));
    }

    //singleton
    public static synchronized MechanicRequests getInstance(){
        if(instance==null)
            instance = new MechanicRequests();
        return instance;
    }

    public void addRequest(Request newR) {
        requests.add(newR);
    }

    public void addPersonal(Request mine){
        if(personal == null)
            personal = mine;
    }

    public Request getPersonal() {
        return personal;
    }

    public void removePersonal() { personal = null; }

    public List<Request> getRequests() {
        return requests;
    }


}
