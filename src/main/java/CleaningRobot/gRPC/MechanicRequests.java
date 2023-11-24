package CleaningRobot.gRPC;

import com.example.chat.CommunicationServiceOuterClass.*;

import java.util.*;

public class MechanicRequests {

    final List<Request> requests;
    Request personal;

    private static MechanicRequests instance;

    private MechanicRequests() {
        requests = new ArrayList<Request>();
    }

    //singleton
    public static synchronized MechanicRequests getInstance(){
        if(instance==null)
            instance = new MechanicRequests();
        return instance;
    }

    public void addRequest(Request newR) {
        synchronized (requests) {
            requests.add(newR);
        }
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
        synchronized (requests) {
            return requests;
        }
    }


}
