package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.breakHandler.robotState;
import com.example.chat.CommunicationServiceGrpc;
import com.example.chat.CommunicationServiceOuterClass;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import sun.misc.Queue;

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

    public static int[] setBots(/*HashMap<Integer, Integer> botPorts, RobotList*/ List<RobotInfo> bots/* RobotInfo singleBot*/) {

        //ci dovrebbe SEMPRE essere almeno bot=me stesso
        //this.bots = bots;
        System.out.println(bots.size());
        int size = bots.size();
        int[] RobotPortInfo = new int[size];

        System.out.println("line 45 p2p");
        System.out.println(bots.get(0).getPortN());

        int i = 0;
        //boolean stopCondition = false;

        while (size > i) { //(!stopCondition) {

            //if (this.bots.get(i) != null) {
            RobotPortInfo[i] = bots.get(i).getPortN();
            //if(this.bots.get(i).getId() == this.botID)
                //this.botDistrict = this.bots.get(i).getId();
            System.out.println(bots.get(i).getPortN());
            i++;
            //} else System.out.println("sono nell'else");
            //stopCondition = true; //finished elements
        }

        //}*/

        return RobotPortInfo;

    }

    public static void firstMSG(/*int[] RobotPortInfo*/List<RobotInfo> listCopy, int botPort, int botDistrict, int botID, int x, int y) throws InterruptedException {

        for (RobotInfo element : listCopy) {

            String target = "localhost:" + element.getPortN();
            System.out.println("sto per creare un channel come target: " + target);

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

                    List<RobotInfo> list = RobotList.getInstance().getRobotslist();
                    System.out.println(list.toString());

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

    public static void requestMechanic(/*int[] RobotPortInfo, */List<RobotInfo> listCopy, int botPort, int botID) throws InterruptedException {

        /*List<RobotInfo> copy = RobotList.getInstance().getRobotslist();
        System.out.println(copy); */
        //inutile: int[] RobotPortInfo = setBots(listCopy);
        List<Integer> authorizations = new ArrayList<>();

        //for (int element : RobotPortInfo) {
        for (RobotInfo element : listCopy) {

            if(element.getPortN() != botPort) { //in questo caso non mando il messaggio a me stesso

                String target = "localhost:" + element.getPortN();
                //System.out.println("sto per creare un channel come target: " + target);

                //plaintext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

                //creating an asynchronous stub on the channel
                CommunicationServiceGrpc.CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

                //creating the HelloResponse object which will be provided as input to the RPC method
                CommunicationServiceOuterClass.Request ask = CommunicationServiceOuterClass.Request.newBuilder()
                        .setFrom(botPort)
                        .setTime(System.currentTimeMillis())
                        .build();

                MechanicRequests.getInstance().addPersonal(ask);

                //calling the RPC method. since it is asynchronous, we need to define handlers
                stub.requestMechanic(ask, new StreamObserver<CommunicationServiceOuterClass.Authorization>() {

                    @Override
                    public void onNext(CommunicationServiceOuterClass.Authorization value) {

                        //quando ricevo la risposta
                        System.out.println("ho ricevuto il si da: " + element.getPortN());
                        authorizations.add(element.getPortN());

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    //streamGreeting(request, new StreamObserver<HelloResponse>() {

                });

                //you need this. otherwise the method will terminate before that answers from the server are received
                channel.awaitTermination(10, TimeUnit.SECONDS);

            }

        }

        System.out.println("ho ricevuto ok da: " + authorizations);

        /*if(authorizations.size() == (listCopy.size() - 1)) {
            System.out.println("ho ricevuto l'ok da tutti, posso andare dal meccanico");
        }*/

        while(authorizations.size() < (listCopy.size() - 1)) {
            System.out.println("waiting for all authorizations");
        }
        System.out.println("ho ricevuto l'ok da tutti, posso andare dal meccanico");
        //sarebbe else wait ?

    }

    public static void answerPending() throws InterruptedException {

        Queue pending = MechanicRequests.getInstance().getRequests();

        if(pending != null) {
            while(!pending.isEmpty()){
                CommunicationServiceOuterClass.Request last = (CommunicationServiceOuterClass.Request) pending.dequeue();


            }
        }

    }

}
