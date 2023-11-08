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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RobotP2P {

    int botID;

    int[] allID;
    static int[] RobotPortInfo; //only the ports
    int botPort;
    List<RobotInfo> bots;
    int botDistrict;

    Socket s;
    //Server gRPCserver;

    private ManagedChannel channel;

    public RobotP2P(/*int botID, int botPort*/) {/*, List<RobotInfo> bots) {*/
        //this.botPorts = botPorts;

        /*this.botID = botID;
        this.botPort = botPort;*/
        //this.bots = bots;

        //setBots();
    }

    public static void firstMSG() throws InterruptedException {

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
            //System.out.println("sto per creare un channel come target: " + target);

            //plaintext channel on the address (ip/port) which offers the GreetingService service
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

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

    public static void lastMSG() throws InterruptedException {

        //rimuovere listCopy e fare una chiamata! no?
        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();

        int myID = RobotInfo.getInstance().getId();
        int myPort = RobotInfo.getInstance().getPortN();

        //creating the HelloResponse object which will be provided as input to the RPC method
        CommunicationServiceOuterClass.Goodbye bye =  CommunicationServiceOuterClass.Goodbye.newBuilder()
                .setFrom(myPort)
                .setId(myID)
                .build();

        //telling other robots !!
        for (RobotInfo element : listCopy) {

            if (element.getId() != myID) {
                String target = "localhost:" + element.getPortN();
                //System.out.println("sto per creare un channel come target: " + target);

                //plaintext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

                //creating an asynchronous stub on the channel
                CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

                //calling the RPC method. since it is asynchronous, we need to define handlers
                stub.removalMsg(bye, new StreamObserver<Empty>() {

                    @Override
                    public void onNext(Empty value) {
                        robotState.getInstance().incrementClock();
                        //List<RobotInfo> list = RobotList.getInstance().getRobotslist();
                        //System.out.println(list.toString());

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

    public static void requestMechanic() throws InterruptedException {

        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();

        int botPort = RobotInfo.getInstance().getPortN();
        System.out.println("my port: " + botPort);

        //ma a cosa mi serve l'ID ?
        int botId = RobotInfo.getInstance().getId();

        CommunicationServiceOuterClass.Request ask = CommunicationServiceOuterClass.Request.newBuilder()
                .setFrom(botPort)
                .setClock(robotState.getInstance().getClock())
                .build();

        //fare una sola volta -- tengo conto della mia richiesta
        MechanicRequests.getInstance().addPersonal(ask);

        //for (int element : RobotPortInfo) {
        for (RobotInfo element : listCopy) {

            if(!Objects.equals(element.getPortN(), botPort)) { //in questo caso non mando il messaggio a me stesso

                robotState.getInstance().incrementClock();

                System.out.println("sto per mandare la richiesta a: " + element.getPortN());

                String target = "localhost:" + element.getPortN();
                //System.out.println("sto per creare un channel come target: " + target);

                //plaintext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

                //creating an asynchronous stub on the channel
                CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

                //calling the RPC method. since it is asynchronous, we need to define handlers
                stub.requestMechanic(ask, new StreamObserver<CommunicationServiceOuterClass.Authorization>() {

                    @Override
                    public void onNext(CommunicationServiceOuterClass.Authorization value) {
                        //quando ricevo la risposta
                        if(value.getOk()) {
                            System.out.println("ho ricevuto il si da: " + element.getPortN());
                            Authorizations.getInstance().addAuthorization(value);
                        }

                    }

                    @Override
                    public void onError(Throwable t) {

                        //dealing with re-organization
                        //posso chiamare una funzione di GRPC da qui dentro? o mi appoggio da qualche altra parte?
                        System.out.println("someone crashed during my requests");
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

    public static void answerPending() throws InterruptedException {

        List pending = MechanicRequests.getInstance().getRequests();
        int size = MechanicRequests.getInstance().getRequests().size();

        int botPort = RobotInfo.getInstance().getPortN();

        System.out.println("size pending requests: ");
        System.out.println(size);

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
            //System.out.println("sto per creare un channel come target: " + target);

            //plaintext channel on the address (ip/port) which offers the GreetingService service
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

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

    public static void organize(int botID) throws InterruptedException {

        //msg to tell to delete it
        //
        //call to server????

        ArrayList<Integer> distribution = RobotPositions.getInstance().getRobotsDistricts();
        System.out.println(distribution);

        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();
        int botPort = RobotInfo.getInstance().getPortN();

        CommunicationServiceOuterClass.UncontrolledCrash robot = CommunicationServiceOuterClass.UncontrolledCrash.newBuilder()
                .setId(botID)
                .build();

        //for (int element : RobotPortInfo) {
        for (RobotInfo element : listCopy) {

            if(element.getPortN() != botPort) { //in questo caso non mando il messaggio a me stesso

                String target = "localhost:" + element.getPortN();
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
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
