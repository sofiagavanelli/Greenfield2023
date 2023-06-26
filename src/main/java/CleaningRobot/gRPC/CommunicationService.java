package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import com.example.chat.CommunicationServiceGrpc;
import com.example.chat.CommunicationServiceOuterClass;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public class CommunicationService extends CommunicationServiceGrpc.CommunicationServiceImplBase {


    @Override
    public void presentationMsg(CommunicationServiceOuterClass.Presentation request, StreamObserver<Empty> responseObserver) {

        System.out.println(request);

        //creo un robot e lo aggiungo
        RobotInfo newBot = new RobotInfo(request.getId(), request.getPort());
        RobotList.getInstance().add(newBot);

        //IMPORTANTE
        //devo fare in modo che se ricevo un messaggio da un nuovo robot allora mi devo memorizzare quella nuova info!!!!!

        System.out.println(request.getId());

        //passo la risposta nello stream
        //Empty response = null;
        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

    @Override
    public void requestMechanic(CommunicationServiceOuterClass.Request request, StreamObserver<CommunicationServiceOuterClass.Authorization> responseObserver) {

        System.out.println("sono dentro request mechanic");
        System.out.println(request);

        //if?

        //costruisco la richiesta di tipo HelloResponse (sempre definito in .proto)
        CommunicationServiceOuterClass.Authorization response = CommunicationServiceOuterClass.Authorization.newBuilder().setOk("OK").build();

        //passo la risposta nello stream
        responseObserver.onNext(response);

    }


}
