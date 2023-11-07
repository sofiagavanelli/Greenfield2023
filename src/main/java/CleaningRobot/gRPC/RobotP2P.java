package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import AdminServer.beans.RobotPositions;
import CleaningRobot.breakHandler.crashSimulator;
import CleaningRobot.breakHandler.robotState;
import com.example.chat.CommunicationServiceGrpc;
import com.example.chat.CommunicationServiceOuterClass;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
//import sun.misc.Queue;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RobotP2P {

    Integer botID;

    Integer[] allID;
    static Integer[] RobotPortInfo; //only the ports
    Integer botPort;
    List<RobotInfo> bots;
    Integer botDistrict;

    Socket s;
    //Server gRPCserver;

    private ManagedChannel channel;

    public RobotP2P(/*Integer botID, Integer botPort*/) {/*, List<RobotInfo> bots) {*/
        //this.botPorts = botPorts;

        /*this.botID = botID;
        this.botPort = botPort;*/
        //this.bots = bots;

        //setBots();
    }

    public static void firstMSG() throws IntegererruptedException {

        //rimuovere listCopy e fare una chiamata! no?
        //rimuovere le info
        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();

        //creating the HelloResponse object which will be provided as input to the RPC method
        CommunicationServiceOuterClass.Presentation broadcast =  CommunicationServiceOuterClass.Presentation.newBuilder()
                .setPort(RobotInfo.getInstance().getPortN())
                .setDistrict(RobotInfo.getInstance().getDistrict())
                .setId(RobotInfo.getInstance().getId())
                .setX(RobotInfo.getInstance().getX())
                .setY(RobotInfo.getInstance().getY())
                .build();

        for (RobotInfo element : listCopy) {

            String target = "localhost:" + element.getPortN();
            //System.out.prIntegerln("sto per creare un channel come target: " + target);

            //plaIntegerext channel on the address (ip/port) which offers the GreetingService service
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaIntegerext().build();

            //creating an asynchronous stub on the channel
            CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.presentationMsg(broadcast, new StreamObserver<Empty>() {

                @Override
                public void onNext(Empty value) {
                    robotState.getInstance().incrementClock();
                }

                @Override
                public void onError(Throwable t) {

                    //tramite on error gestisco se un robot è saltato ??

                }

                @Override
                public void onCompleted() {

                    channel.shutdownNow();

                }

            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            channel.awaitTermination(10, TimeUnit.SECONDS);

        }

    }

    public static void lastMSG() throws IntegererruptedException {

        //rimuovere listCopy e fare una chiamata! no?
        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();

        Integer myID = RobotInfo.getInstance().getId();
        Integer myPort = RobotInfo.getInstance().getPortN();

        //creating the HelloResponse object which will be provided as input to the RPC method
        CommunicationServiceOuterClass.Goodbye bye =  CommunicationServiceOuterClass.Goodbye.newBuilder()
                .setFrom(myPort)
                .setId(myID)
                .build();

        //telling other robots !!
        for (RobotInfo element : listCopy) {

            if (element.getId() != myID) {
                String target = "localhost:" + element.getPortN();
                //System.out.prIntegerln("sto per creare un channel come target: " + target);

                //plaIntegerext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaIntegerext().build();

                //creating an asynchronous stub on the channel
                CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

                //calling the RPC method. since it is asynchronous, we need to define handlers
                stub.removalMsg(bye, new StreamObserver<Empty>() {

                    @Override
                    public void onNext(Empty value) {
                        robotState.getInstance().incrementClock();
                        //List<RobotInfo> list = RobotList.getInstance().getRobotslist();
                        //System.out.prIntegerln(list.toString());

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {

                        channel.shutdownNow();

                    }

                });

                //you need this. otherwise the method will terminate before that answers from the server are received
                channel.awaitTermination(10, TimeUnit.SECONDS);

            }
        }

    }

    public static void requestMechanic(/*Integer[] RobotPortInfo, *//*List<RobotInfo> listCopy, Integer botPort, Integer botID*/) throws IntegererruptedException {

        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();
        Integer botPort = RobotInfo.getInstance().getPortN();

        //ma a cosa mi serve l'ID ?
        Integer botId = RobotInfo.getInstance().getId();

        CommunicationServiceOuterClass.Request ask = CommunicationServiceOuterClass.Request.newBuilder()
                .setFrom(botPort)
                .setClock(robotState.getInstance().getClock())
                .build();

        //fare una sola volta -- tengo conto della mia richiesta
        MechanicRequests.getInstance().addPersonal(ask);

        //for (Integer element : RobotPortInfo) {
        for (RobotInfo element : listCopy) {

            if(element.getPortN() != botPort) { //in questo caso non mando il messaggio a me stesso

                robotState.getInstance().incrementClock();

                System.out.prIntegerln("sto per mandare la richiesta a: " + element.getPortN());

                String target = "localhost:" + element.getPortN();
                //System.out.prIntegerln("sto per creare un channel come target: " + target);

                //plaIntegerext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaIntegerext().build();

                //creating an asynchronous stub on the channel
                CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

                //calling the RPC method. since it is asynchronous, we need to define handlers
                stub.requestMechanic(ask, new StreamObserver<CommunicationServiceOuterClass.Authorization>() {

                    @Override
                    public void onNext(CommunicationServiceOuterClass.Authorization value) {
                        //quando ricevo la risposta
                        if(value.getOk()) {
                            System.out.prIntegerln("ho ricevuto il si da: " + element.getPortN());
                            Authorizations.getInstance().addAuthorization(value);
                        }

                    }

                    @Override
                    public void onError(Throwable t) {

                        //dealing with re-organization
                        //posso chiamare una funzione di GRPC da qui dentro? o mi appoggio da qualche altra parte?
                        System.out.prIntegerln("someone crashed during my requests");
                        RobotList.getInstance().remove(element.getId());
                        crashSimulator.dealUncontrolledCrash(element.getId());
                    }

                    @Override
                    public void onCompleted() {
                        //channel.shutdownNow();
                    }

                });

                //you need this. otherwise the method will terminate before that answers from the server are received
                channel.awaitTermination(10, TimeUnit.SECONDS);

            }

        }

    }

    public static void answerPending() throws IntegererruptedException {

        List pending = MechanicRequests.getInstance().getRequests();
        Integer size = MechanicRequests.getInstance().getRequests().size();

        Integer botPort = RobotInfo.getInstance().getPortN();

        System.out.prIntegerln("size pending requests: ");
        System.out.prIntegerln(size);

        //creating the HelloResponse object which will be provided as input to the RPC method
        CommunicationServiceOuterClass.Authorization answer = CommunicationServiceOuterClass.Authorization.newBuilder()
                .setFrom(botPort)
                .setOk(true)
                .build();

        while(size > 0) {
            robotState.getInstance().incrementClock();
            //se size=1, index=0 !!
            CommunicationServiceOuterClass.Request last = (CommunicationServiceOuterClass.Request) pending.remove((size-1));

            String target = "localhost:" + last.getFrom();
            //System.out.prIntegerln("sto per creare un channel come target: " + target);

            //plaIntegerext channel on the address (ip/port) which offers the GreetingService service
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaIntegerext().build();

            //creating an asynchronous stub on the channel
            CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.answerPending(answer, new StreamObserver<Empty>() {

                @Override
                public void onNext(Empty empty) {

                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {

                }

            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            channel.awaitTermination(10, TimeUnit.SECONDS);

            size = size - 1;


        }

    }

    public static void organize(Integer botID) throws IntegererruptedException {

        //msg to tell to delete it
        //
        //call to server????

        ArrayList<Integereger> distribution = RobotPositions.getInstance().getRobotsDistricts();
        System.out.prIntegerln(distribution);

        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();
        Integer botPort = RobotInfo.getInstance().getPortN();

        CommunicationServiceOuterClass.UncontrolledCrash robot = CommunicationServiceOuterClass.UncontrolledCrash.newBuilder()
                .setId(botID)
                .build();

        //for (Integer element : RobotPortInfo) {
        for (RobotInfo element : listCopy) {

            if(element.getPortN() != botPort) { //in questo caso non mando il messaggio a me stesso

                String target = "localhost:" + element.getPortN();
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaIntegerext().build();
                CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

                stub.organize(robot, new StreamObserver<Empty>() {


                    @Override
                    public void onNext(Empty empty) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });

                channel.awaitTermination(10, TimeUnit.SECONDS);

            }

        }
    }

}
