package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.breakHandler.STATE;
import CleaningRobot.breakHandler.robotState;
import CleaningRobot.personalInfo;
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

        personalInfo.getInstance().incrementClock();

        RobotList.getInstance().remove(request.getId());

        //questi due non sono in contraddizione ?

        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

    @Override
    public void presentationMsg(CommunicationServiceOuterClass.Presentation request, StreamObserver<Empty> responseObserver) {

        System.out.println(request);

        personalInfo.getInstance().incrementClock();

        //creo un robot e lo aggiungo
        //int id, int portN, int x, int y, int district
        RobotInfo newBot = new RobotInfo(request.getId(), request.getPort(), request.getX(), request.getY(), request.getDistrict());
        RobotList.getInstance().add(newBot);

        //se io sono in attesa e un robot è appena entrato allora devo aggiungere
        // la sua autorizzazione alla mia richiesta in corso
        if(robotState.getInstance().getState() == STATE.NEEDING) {
            CommunicationServiceOuterClass.Authorization answer = CommunicationServiceOuterClass.Authorization.newBuilder()
                    .setFrom(request.getPort())
                    .setOk(true)
                    .build();

            Authorizations.getInstance().addAuthorization(answer);
            Authorizations.getInstance().controlAuthorizations();

        }

        //passo la risposta nello stream
        //Empty response = null;
        //onNext is the callback
        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

    @Override
    public void requestMechanic(CommunicationServiceOuterClass.Request request, StreamObserver<CommunicationServiceOuterClass.Authorization> responseObserver) {

        //LAMPORT ?
        System.out.println("my clock: " + personalInfo.getInstance().getClock());
        System.out.println("the sender's clock: " + request.getClock());
        int senderClock = request.getClock();
        int newClock = (Math.max(senderClock, personalInfo.getInstance().getClock()) + 1);
        personalInfo.getInstance().setClock(newClock);
        System.out.println("my new clock: " + personalInfo.getInstance().getClock());

        //qui sono dentro chi riceve
        System.out.println("somebody needs the mechanic");

        CommunicationServiceOuterClass.Authorization response;

        //devo dire da chi è!! mi serve il mio botPort (ma voglio aggiungere l'id?)
        int myPort = personalInfo.getInstance().getPort();

        //i need the mechanic so i have a request out
        if(robotState.getInstance().getState() == STATE.NEEDING) {

            CommunicationServiceOuterClass.Request mine = MechanicRequests.getInstance().getPersonal();

            if(mine.getTime() < request.getTime()) {
                //metto la tua richiesta nella mia coda
                MechanicRequests.getInstance().addRequest(request);
                //ho già io una richiesta ed è inferiore quindi dico di no
                response = CommunicationServiceOuterClass.Authorization.newBuilder()
                        .setOk(false)
                        .setFrom(myPort)
                        .build();
                // wait?
            }
            else //la mia richiesta è successiva quindi hai tu accesso
                response = CommunicationServiceOuterClass.Authorization.newBuilder()
                        .setOk(true)
                        .setFrom(myPort)
                        .build();

        } else if (robotState.getInstance().getState() == STATE.MECHANIC) {
            MechanicRequests.getInstance().addRequest(request);
            //sono dal meccanico quindi rispondo di no
            response = CommunicationServiceOuterClass.Authorization.newBuilder()
                    .setOk(false)
                    .setFrom(myPort)
                    .build();
        } else { //non c'è una mia richiesta
            response = CommunicationServiceOuterClass.Authorization.newBuilder()
                    .setOk(true)
                    .setFrom(myPort)
                    .build();
            //passo la risposta nello stream
        }

        responseObserver.onNext(response);

        responseObserver.onCompleted();

    }

    @Override
    public void answerPending(CommunicationServiceOuterClass.Authorization authorization, StreamObserver<Empty> responseObserver) {

        System.out.println("somebody released the mechanic");
        //chi è in attesa riceve una risposta ?
        personalInfo.getInstance().incrementClock();

        Authorizations.getInstance().addAuthorization(authorization);
        //non posso metterla qui perché il wait viene fatto dentro il mechanic ?
        //Authorizations.getInstance().unblockAuthorizations();
        Authorizations.getInstance().unblockMechanic();

        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }
}
