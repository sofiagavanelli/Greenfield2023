package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import com.example.chat.CommunicationServiceGrpc;
import com.example.chat.CommunicationServiceOuterClass;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class CommunicationService extends CommunicationServiceGrpc.CommunicationServiceImplBase {


    @Override
    public void removalMsg(CommunicationServiceOuterClass.Goodbye request, StreamObserver<Empty> responseObserver) {

        System.out.println(request);

        RobotList.getInstance().remove(request.getId());

        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

    @Override
    public void presentationMsg(CommunicationServiceOuterClass.Presentation request, StreamObserver<Empty> responseObserver) {

        System.out.println(request);

        //creo un robot e lo aggiungo
        //int id, int portN, int x, int y, int district
        RobotInfo newBot = new RobotInfo(request.getId(), request.getPort(), request.getX(), request.getY(), request.getDistrict());
        RobotList.getInstance().add(newBot);

        //passo la risposta nello stream
        //Empty response = null;
        //onNext is the callback
        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

    @Override
    public void requestMechanic(CommunicationServiceOuterClass.Request request, StreamObserver<CommunicationServiceOuterClass.Authorization> responseObserver) {

        //qui sono dentro chi riceve
        System.out.println("sono dentro request mechanic");
        System.out.println(request);

        CommunicationServiceOuterClass.Authorization response;

        if(MechanicRequests.getInstance().getPersonal() != null) {
            //bisognerebbe controllare i timestamp, qui sto dando per scontato che quella nuova sia successiva

            CommunicationServiceOuterClass.Request mine = MechanicRequests.getInstance().getPersonal();

            if(mine.getTime() < request.getTime()) {
                //metto la tua richiesta nella mia coda
                MechanicRequests.getInstance().addRequest(request);
                //ho già io una richiesta ed è inferiore quindi dico di no
                response = CommunicationServiceOuterClass.Authorization.newBuilder().setOk("NO").build();
                // wait?
            }
            else //la mia richiesta è successiva quindi hai tu accesso
                response = CommunicationServiceOuterClass.Authorization.newBuilder().setOk("OK").build();

        }
        else { //non c'è una mia richiesta
            //costruisco la richiesta di tipo HelloResponse (sempre definito in .proto)
            response = CommunicationServiceOuterClass.Authorization.newBuilder().setOk("OK").build();
            //passo la risposta nello stream
        }

        responseObserver.onNext(response);

    }


}
