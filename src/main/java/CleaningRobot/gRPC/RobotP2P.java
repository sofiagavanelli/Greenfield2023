package CleaningRobot.gRPC;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.breakHandler.STATE;
import CleaningRobot.breakHandler.crashSimulator;
import CleaningRobot.breakHandler.robotState;
import Utils.RestFunc;
import com.example.chat.CommunicationServiceGrpc;
import com.example.chat.CommunicationServiceGrpc.*;
import com.example.chat.CommunicationServiceOuterClass;
import com.example.chat.CommunicationServiceOuterClass.*;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RobotP2P {

    private static final Logger logger = Logger.getLogger(RobotP2P.class.getSimpleName());

    public RobotP2P() {
    }

    public static void firstMSG() {

        //rimuovere le info
        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();

        //creating the response object which will be provided as input to the RPC method
        Presentation broadcast =  Presentation.newBuilder()
                .setPort(RobotInfo.getInstance().getPortN())
                .setDistrict(RobotInfo.getInstance().getDistrict())
                .setId(RobotInfo.getInstance().getId())
                .setX(RobotInfo.getInstance().getX())
                .setY(RobotInfo.getInstance().getY())
                .setClock(robotState.getInstance().getClock())
                .build();


        for (RobotInfo element : listCopy) {

            robotState.getInstance().incrementClock();

            String target = "localhost:" + element.getPortN();

            //plaintext channel on the address (ip/port) which offers the GreetingService service
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

            //creating an asynchronous stub on the channel
            CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.presentationMsg(broadcast, new StreamObserver<Empty>() {

                @Override
                public void onNext(Empty value) {

                }

                @Override
                public void onError(Throwable t) {
                    organizeGrid(element.getId(), element.getDistrict());
                }

                @Override
                public void onCompleted() {
                    channel.shutdownNow();
                }

            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            /*try {
                channel.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.severe("Problems with message sending");
            }*/

        }

    }

    public static void lastMSG() {

        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();

        int myID = RobotInfo.getInstance().getId();
        int myPort = RobotInfo.getInstance().getPortN();
        int myD = RobotInfo.getInstance().getDistrict();

        Goodbye bye =  Goodbye.newBuilder()
                .setFrom(myPort)
                .setId(myID)
                .setDistrict(myD)
                .setClock(robotState.getInstance().getClock())
                .build();

        //telling other robots !!
        for (RobotInfo element : listCopy) {

            if (element.getId() != myID) {
                //prima di ognuno
                robotState.getInstance().incrementClock();

                String target = "localhost:" + element.getPortN();

                //plaintext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

                //creating an asynchronous stub on the channel
                CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

                //calling the RPC method. since it is asynchronous, we need to define handlers
                stub.removalMsg(bye, new StreamObserver<Empty>() {

                    @Override
                    public void onNext(Empty value) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        organizeGrid(element.getId(), element.getDistrict());
                    }

                    @Override
                    public void onCompleted() {
                        channel.shutdownNow();
                    }

                });

                //you need this. otherwise the method will terminate before that answers from the server are received
                /*try {
                    channel.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.severe("Problems with message sending");
                }*/

            }
        }

    }

    public static void requestMechanic() {

        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();

        int botPort = RobotInfo.getInstance().getPortN();

        //should i increment the clock?
        Request ask = CommunicationServiceOuterClass.Request.newBuilder()
                .setFrom(botPort)
                //clock time AT THE MOMENT
                .setClock(robotState.getInstance().getClock())
                .build();

        //tengo conto della mia richiesta
        MechanicRequests.getInstance().addPersonal(ask);

        for (RobotInfo element : listCopy) {

            //in questo caso non mando il messaggio a me stesso
            if(element.getPortN() == botPort)
                continue;

            robotState.getInstance().incrementClock();

            //logger.info("I'm sending my mechanic request to: " + element.getPortN());

            String target = "localhost:" + element.getPortN();

            //plaintext channel on the address (ip/port) which offers the GreetingService service
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

            //creating an asynchronous stub on the channel
            CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.requestMechanic(ask, new StreamObserver<CommunicationServiceOuterClass.Authorization>() {

                @Override
                public void onNext(CommunicationServiceOuterClass.Authorization value) {
                    //quando ricevo la risposta
                    if(value.getOk()) {
                        logger.info("I received an authorization from: " + element.getPortN());
                        Authorizations.getInstance().addAuthorization(value);
                    }

                }

                @Override
                public void onError(Throwable t) {
                    organizeGrid(element.getId(), element.getDistrict());
                }

                @Override
                public void onCompleted() {
                    channel.shutdownNow();
                }

            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            /*try {
                channel.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.severe("Problems with message sending");
            }*/


        }

    }

    public static void answerPending() {

        List<CommunicationServiceOuterClass.Request> pending = MechanicRequests.getInstance().getRequests();
        int size = MechanicRequests.getInstance().getRequests().size();

        int botPort = RobotInfo.getInstance().getPortN();

        //creating the HelloResponse object which will be provided as input to the RPC method
        Authorization answer = CommunicationServiceOuterClass.Authorization.newBuilder()
                .setFrom(botPort)
                .setOk(true)
                .setClock(robotState.getInstance().getClock())
                .build();

        while(size > 0) {
            robotState.getInstance().incrementClock();
            //se size=1, index=0 !!
            CommunicationServiceOuterClass.Request last = pending.remove((size-1));

            String target = "localhost:" + last.getFrom();
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
            CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.answerPending(answer, new StreamObserver<Empty>() {

                @Override
                public void onNext(Empty empty) {

                }

                @Override
                public void onError(Throwable t) {
                    //
                    getByPort(last.getFrom());
                }

                @Override
                public void onCompleted() {
                    channel.shutdownNow();
                }

            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            /*try {
                channel.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.severe("Problems with message sending");
            }*/

            size = size - 1;

        }

    }

    public static void organize(int botID, int botDistrict) {

        List<RobotInfo> listCopy = RobotList.getInstance().getRobotslist();
        int botPort = RobotInfo.getInstance().getPortN();

        //these are the crashed robot's information
        UncontrolledCrash robot = CommunicationServiceOuterClass.UncontrolledCrash.newBuilder()
                .setId(botID)
                .setDistrict(botDistrict)
                .setClock(robotState.getInstance().getClock())
                .build();

        //for (int element : RobotPortInfo) {
        for (RobotInfo element : listCopy) {

            if(element.getPortN() != botPort) { //in questo caso non mando il messaggio a me stesso

                String target = "localhost:" + element.getPortN();
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

                CommunicationServiceStub stub = CommunicationServiceGrpc.newStub(channel);

                stub.organize(robot, new StreamObserver<Empty>() {


                    @Override
                    public void onNext(Empty empty) {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        //get by port?
                    }

                    @Override
                    public void onCompleted() {
                        channel.shutdownNow();
                    }
                });

                /*try {
                    channel.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.severe("Problems with message sending");
                }*/

            }

        }
    }


    public static void organizeGrid(int id, int district) {
        //if it's still here, maybe in the meantime i have received a message to delete it

        //dealing with re-organization
        logger.warning("Someone crashed during my message");
        int requestPort = RobotList.getInstance().getPortById(id);

        //SHOULD I FIRST SEND THE MSG TO EVERYBODY? TO BE THE LEAST AMOUNT OF TIME WITHOUT THIS INFORMATION
        //i tell everybody who has died
        organize(id, district);

        //i inform the server
        RestFunc.deleteRobot(id);
        //i understand who needs to move and i delete him
        crashSimulator.dealUncontrolledCrash(id);

        //if i needed just him then i can go now!!!!!
        if(robotState.getInstance().getState() == STATE.NEEDING) {
            if(Authorizations.getInstance().isPresent(requestPort)) {
                Authorizations.getInstance().removeOne(requestPort);
            }
            Authorizations.getInstance().unblockMechanic();
        }

    }

    public static void getByPort(int port) {

        List<RobotInfo> copy = RobotList.getInstance().getRobotslist();

        for(RobotInfo r : copy) {
            if(r.getPortN() == port) {
                organizeGrid(r.getId(), r.getDistrict());
            }
        }

    }

}
