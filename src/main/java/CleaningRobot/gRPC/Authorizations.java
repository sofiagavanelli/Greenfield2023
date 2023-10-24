package CleaningRobot.gRPC;

import com.example.chat.CommunicationServiceOuterClass;
import sun.misc.Queue;

public class Authorizations {

    Queue<CommunicationServiceOuterClass.Authorization> authorizations;

    private static Authorizations instance;

    //singleton
    public static synchronized Authorizations getInstance(){
        if(instance==null)
            instance = new Authorizations();
        return instance;
    }

    public void addAuthorization(CommunicationServiceOuterClass.Authorization newA) {
        authorizations.enqueue(newA);
    }

    public Queue<CommunicationServiceOuterClass.Authorization> getAuthorizations() {
        return authorizations;
    }

}
