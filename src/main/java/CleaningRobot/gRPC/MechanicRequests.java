package CleaningRobot.gRPC;

import CleaningRobot.breakHandler.robotState;
import com.example.chat.CommunicationServiceOuterClass;
import sun.misc.Queue;

import java.util.List;

public class MechanicRequests {

    Queue<CommunicationServiceOuterClass.Request> requests;
    CommunicationServiceOuterClass.Request personal;

    private static MechanicRequests instance;

    //singleton
    public static synchronized MechanicRequests getInstance(){
        if(instance==null)
            instance = new MechanicRequests();
        return instance;
    }

    public void addRequest(CommunicationServiceOuterClass.Request newR) {
        requests.enqueue(newR);
    }

    public void addPersonal(CommunicationServiceOuterClass.Request mine){
        personal = mine;
    }

    public CommunicationServiceOuterClass.Request getPersonal() {
        return personal;
    }

    public Queue<CommunicationServiceOuterClass.Request> getRequests() {
        return requests;
    }


}
