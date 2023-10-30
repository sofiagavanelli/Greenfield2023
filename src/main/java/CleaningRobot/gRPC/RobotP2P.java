package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.breakHandler.robotState;
import CleaningRobot.personalInfo;
import com.example.chat.CommunicationServiceGrpc;
import com.example.chat.CommunicationServiceOuterClass;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
//import sun.misc.Queue;
import java.util.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

    public static void firstMSG(/*int[] RobotPortInfo*/List<RobotInfo> listCopy, int botPort, int botDistrict, int botID, int x, int y) throws InterruptedException {

        //rimuovere listCopy e fare una chiamata! no?

        for (RobotInfo element : listCopy) {

            String target = "localhost:" + element.getPortN();
            //System.out.println("sto per creare un channel come target: " + target);

            //plaintext channel on the address (ip/port) which offers the GreetingService service
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

            //creating an asynchronous stub on the channel
            CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

            //creating the HelloResponse object which will be provided as input to the RPC method
            CommunicationServiceOuterClass.Presentation broadcast =  CommunicationServiceOuterClass.Presentation.newBuilder()
                    .setPort(botPort)
                    .setDistrict(botDistrict)
                    .setId(botID)
                    .setX(x)
                    .setY(y)
                    .build();

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.presentationMsg(broadcast, new StreamObserver<Empty>() {

                @Override
                public void onNext(Empty value) {

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

    public static void requestMechanic(/*int[] RobotPortInfo, *//*List<RobotInfo> listCopy, int botPort, int botID*/) throws InterruptedException {

        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();
        int botPort = personalInfo.getInstance().getPort();

        //ma a cosa mi serve l'ID ?
        int botId = personalInfo.getInstance().getBotID();

        CommunicationServiceOuterClass.Request ask = CommunicationServiceOuterClass.Request.newBuilder()
                .setFrom(botPort)
                .setTime(System.currentTimeMillis())
                .build();

        //fare una sola volta -- tengo conto della mia richiesta
        MechanicRequests.getInstance().addPersonal(ask);

        //for (int element : RobotPortInfo) {
        for (RobotInfo element : listCopy) {

            if(element.getPortN() != botPort) { //in questo caso non mando il messaggio a me stesso

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

                    }

                    @Override
                    public void onCompleted() {
                        //channel.shutdownNow();
                    }

                    //streamGreeting(request, new StreamObserver<HelloResponse>() {

                });

                //you need this. otherwise the method will terminate before that answers from the server are received
                channel.awaitTermination(10, TimeUnit.SECONDS);

            }

        }

        //System.out.println("ho ricevuto ok da: " + authorizations);

        /*if(authorizations.size() == (listCopy.size() - 1)) {
            System.out.println("ho ricevuto l'ok da tutti, posso andare dal meccanico");
        }*/

        //if(authorizations.size() < (listCopy.size() - 1)) {
            //System.out.println("waiting for all authorizations");
        //}
        //System.out.println("ho ricevuto l'ok da tutti, posso andare dal meccanico");
        //sarebbe else wait ?

    }

    public static void answerPending(/*int botPort*/) throws InterruptedException {

        List pending = MechanicRequests.getInstance().getRequests();
        int size = MechanicRequests.getInstance().getRequests().size();

        int botPort = personalInfo.getInstance().getPort();

        System.out.println("size pending requests: ");
        System.out.println(size);

        while(size > 0) {
            //se size=1, index=0 !!
            CommunicationServiceOuterClass.Request last = (CommunicationServiceOuterClass.Request) pending.remove((size-1));

            String target = "localhost:" + last.getFrom();
            //System.out.println("sto per creare un channel come target: " + target);

            //plaintext channel on the address (ip/port) which offers the GreetingService service
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

            //creating an asynchronous stub on the channel
            CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

            //creating the HelloResponse object which will be provided as input to the RPC method
            CommunicationServiceOuterClass.Authorization answer = CommunicationServiceOuterClass.Authorization.newBuilder()
                    .setFrom(botPort)
                    .setOk(true)
                    .build();

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

}
