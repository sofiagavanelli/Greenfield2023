package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import AdminServer.beans.RobotPositions;
import CleaningRobot.breakHandler.STATE;
import CleaningRobot.breakHandler.crashSimulator;
import CleaningRobot.breakHandler.robotState;
import com.example.chat.CommunicationServiceGrpc;
import com.example.chat.CommunicationServiceOuterClass;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Logger;

public class CommunicationService extends CommunicationServiceGrpc.CommunicationServiceImplBase {

    private static final Logger logger = Logger.getLogger(CommunicationService.class.getSimpleName());

    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %3$s : %5$s %n");
    }

    @Override
    public void removalMsg(CommunicationServiceOuterClass.Goodbye request, StreamObserver<Empty> responseObserver) {

        logger.info("A ROBOT-" + request.getId() + " left Greenfield ");

        robotState.getInstance().adjustClock(request.getClock());

        RobotList.getInstance().remove(request.getId());
        RobotPositions.getInstance().removeFromDistribution(request.getDistrict(), request.getId());

        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

    @Override
    public void presentationMsg(CommunicationServiceOuterClass.Presentation request, StreamObserver<Empty> responseObserver) {

        logger.info("There is a new robot in Greenfield: ROBOT-" + request.getId() + " in district " + request.getDistrict());

        robotState.getInstance().adjustClock(request.getClock());

        //int id, int portN, int x, int y, int district
        RobotInfo newBot = new RobotInfo(request.getId(), request.getPort(), request.getX(), request.getY(), request.getDistrict());
        RobotList.getInstance().add(newBot);
        //i add to the distribution
        RobotPositions.getInstance().addIntoDistribution(request.getDistrict(), request.getId());

        //se io sono in attesa e un robot è appena entrato allora devo aggiungere
        //la sua autorizzazione alla mia richiesta in corso
        if(robotState.getInstance().getState() == STATE.NEEDING) {
            CommunicationServiceOuterClass.Authorization answer = CommunicationServiceOuterClass.Authorization.newBuilder()
                    .setFrom(request.getPort())
                    .setOk(true)
                    .build();

            Authorizations.getInstance().addAuthorization(answer);
            Authorizations.getInstance().controlAuthorizations();
        }

        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

    @Override
    public void requestMechanic(CommunicationServiceOuterClass.Request request, StreamObserver<CommunicationServiceOuterClass.Authorization> responseObserver) {

        //LAMPORT
        robotState.getInstance().adjustClock(request.getClock());

        CommunicationServiceOuterClass.Authorization response;

        int myPort = RobotInfo.getInstance().getPortN();

        //i need the mechanic too so i have a request out
        if(robotState.getInstance().getState() == STATE.NEEDING) {

            CommunicationServiceOuterClass.Request mine = MechanicRequests.getInstance().getPersonal();

            if(mine.getClock() < request.getClock()) {
                //metto la tua richiesta nella mia coda
                MechanicRequests.getInstance().addRequest(request);
                //ho già io una richiesta ed è inferiore quindi dico di no
                response = CommunicationServiceOuterClass.Authorization.newBuilder()
                        .setOk(false)
                        .setFrom(myPort)
                        .build();
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

        logger.info("Somebody released the mechanic");

        robotState.getInstance().adjustClock(authorization.getClock());

        Authorizations.getInstance().addAuthorization(authorization);

        Authorizations.getInstance().unblockMechanic();

        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }

    @Override
    public void organize(CommunicationServiceOuterClass.UncontrolledCrash request, StreamObserver<Empty> responseObserver) {

        //call to remove
        logger.info("Somebody told me to remove " + request.getId() + " because it has crashed");

        robotState.getInstance().adjustClock(request.getClock());
        //i calculate who needs to move and i remove him
        crashSimulator.dealUncontrolledCrash(request.getId());

        //if i'm waiting to obtain its authorization?
        //what happens if i notify and nobody is waiting?
        //if i'm needing i need to control
        //what if he said ok and now i need to remove also its authorization
        if(robotState.getInstance().getState() == STATE.NEEDING) {
            if(Authorizations.getInstance().isPresent(request.getId())) {
                Authorizations.getInstance().removeOne(request.getId());
            }
        }

        responseObserver.onNext(null);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }
}
