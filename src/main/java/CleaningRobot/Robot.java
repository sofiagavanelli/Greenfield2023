package CleaningRobot;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.MQTT.MqttPub;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.breakHandler.Mechanic;
import CleaningRobot.breakHandler.STATE;
import CleaningRobot.breakHandler.crashSimulator;
import CleaningRobot.breakHandler.robotState;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.gRPC.CommunicationService;
import CleaningRobot.simulators.PM10Simulator;
import CleaningRobot.simulators.WindowBuffer;
import Utils.RestFunc;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Robot {

    private static Integer botId;
    private static Integer botPort;
    private Integer botDistrict;
    private Integer x;
    private Integer y;

    static Random rnd = new Random();

    List<RobotInfo> robotInfoList;
    Integer[] RobotPortInfo;

    //static
    WindowBuffer newB; // = new WindowBuffer(8);
    //static
    Reader readSensor; // = new Reader(newB);
    //static
    PM10Simulator botSimulator; // = new PM10Simulator(newB);
    Mechanic mechanicHandler;
    crashSimulator crashTest;
    RobotP2P gRPCclient;
    Server gRPCserver;
    MqttPub pub;

    ClientConfig config = new DefaultClientConfig();
    Client client = Client.create(config);
    String serverAddress = "http://localhost:1337";
    ClientResponse clientResponse = null;

    public Robot() {

    }

    void initialize() {

        WindowBuffer newB = new WindowBuffer(8);
        this.newB = newB;
        PM10Simulator botSimulator = new PM10Simulator(newB);
        this.botSimulator = botSimulator;
        Reader readSensor = new Reader(newB);
        this.readSensor = readSensor;
        Mechanic mechanicHandler = new Mechanic();
        this.mechanicHandler = mechanicHandler;
        crashSimulator crashTest = new crashSimulator();
        this.crashTest = crashTest;

        //Robot bot = new Robot(botId, botPort, botSimulator, readSensor, newB, crashTest, gRPCclient);

        RestFunc.addNewRobot(botId, botPort);

        // POST EXAMPLE
        /*String postPath = "/robots/add";
        RobotInfo bot1 = new RobotInfo(botId, botPort);
        clientResponse = RestFunc.postRequest(client,serverAddress+postPath, bot1);
        System.out.println(clientResponse.toString());

        //qui cosa sto facendo: chiedo al server la lista dei robot
        RobotList robs = clientResponse.getEntity(RobotList.class);
        //la copio ?
        List<RobotInfo> copyRobs = robs.getRobotslist();
        RobotList.getInstance().setRobotslist(copyRobs);*/

        //then you should receive back the position and the district !!!!
        //inserire una funzione!!!

        //si potrebbero togliere gli assegnamenti ?
        /*for (RobotInfo r : robs.getRobotslist()){
            if(r.getId() == botId) {
                botDistrict = r.getDistrict();
                x = r.getX();
                y = r.getY();
            }
        }

        personalInfo.getInstance().setAll(botId, botDistrict, x, y, botPort);*/

        System.out.println("the current position is in the district: " + botDistrict + " x: " + x + " y: " + y);

        //MqttPub : ogni robot ha il suo publisher
        MqttPub pub = new MqttPub(Integer.toString(RobotInfo.getInstance().getDistrict()), readSensor, botId);
        this.pub = pub;
        this.pub.start(); //start or run?

        //this.gRPCclient.start();
        this.startGRPCServer();

        try {
            //anche questo passaggio delle liste !!!! no.
            //List<RobotInfo> list = RobotList.getInstance().getRobotslist();
            //Integer[] RobotPortInfo, Integer botPort, Integer botDistrict, Integer botID
            //si può togliere tutto dagli input
            RobotP2P.firstMSG();
        }  catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.botSimulator.start();
        this.readSensor.start();
        //set connections probabilmente non serve più!!
        //this.mechanicHandler.setConnections(RobotPortInfo);

        this.crashTest.start();

        this.mechanicHandler.start();

    }

    public void stop() {

        try {
            RobotP2P.lastMSG();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        RestFunc.deleteRobot(RobotInfo.getInstance().getId());

        //not failing anymore
        crashTest.stopCrash();

        botSimulator.stopMeGently();
        readSensor.stopReading();

        gRPCserver.shutdown();
        pub.stopPublishing();

        mechanicHandler.stopMechanic();

        //do i need this?
        System.exit(0);

    }

    private void startGRPCServer() {

        gRPCserver = ServerBuilder.forPort(botPort)
                .addService(new CommunicationService())
                .build();

        try {
            gRPCserver.start();
        }
        catch (Exception e) {

        }
    }

    public static void main(String argv[]) throws Exception {

        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        System.out.print("Enter id: ");
        botId = sc.nextInt();
        System.out.print("Enter port: ");
        botPort = sc.nextInt();

        //qui?
        boolean stopCondition = false;

        Robot bot = new Robot();
        bot.initialize();

        while(!stopCondition) {

            String cmd = sc.nextLine();

            if(cmd.equalsIgnoreCase("fix"))
                crashSimulator.signalCrash();
            else if (cmd.equalsIgnoreCase("quit"))
                bot.stop();

        }

    }

    /*public static void main(String argv[]) throws Exception {

        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        System.out.print("Enter id: ");
        botId = sc.nextInteger();
        System.out.print("Enter port: ");
        botPort = sc.nextInteger();


        WindowBuffer newB = new WindowBuffer(8);
        PM10Simulator botSimulator = new PM10Simulator(newB);
        Reader readSensor = new Reader(newB);
        Mechanic mechanicHandler = new Mechanic(botSimulator, readSensor, botId, botPort);
        crashSimulator crashTest = new crashSimulator();
        //RobotP2P gRPCclient = new RobotP2P(botId, botPort);

        botDistrict = 0; //non dovrebbe servire
        //cambiare botId in String? e mettere "ROBOT-n" ?
        //botId = rnd.nextInteger(100) + 10;
        //botPort = 1234; //input??

        //RobotP2P gRPCclient = new RobotP2P(botId, botPort);
        Robot bot = new Robot(botId, botPort, botSimulator, readSensor, newB, mechanicHandler, crashTest);

        //Robot bot = new Robot(botId, botPort, botSimulator, readSensor, newB, crashTest, gRPCclient);

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        String serverAddress = "http://localhost:1337";
        ClientResponse clientResponse = null;

        // POST EXAMPLE
        String postPath = "/robots/add";
        RobotInfo bot1 = new RobotInfo(botId, botPort);
        clientResponse = RestFunc.postRequest(client,serverAddress+postPath, bot1);
        System.out.println(clientResponse.toString());

        //qui cosa sto facendo: chiedo al server la lista dei robot
        RobotList robs = clientResponse.getEntity(RobotList.class);
        //la copio ?
        List<RobotInfo> copyRobs = robs.getRobotslist();
        RobotList.getInstance().setRobotslist(copyRobs);
        //gRPCclient.setBots(copyRobs);

        //then you should receive back the position and the district !!!!
        //inserire una funzione!!!

        //si potrebbero togliere gli assegnamenti ?
        for (RobotInfo r : robs.getRobotslist()){
            if(r.getId() == botId) {
                botDistrict = r.getDistrict();
                x = r.getX();
                y = r.getY();
            }
        }

        personalInfo.getInstance().setAll(botId, botDistrict, x, y, botPort);

        System.out.println("the current position is in the district: " + botDistrict + " x: " + x + " y: " + y);

        //MqttPub : ogni robot ha il suo publisher
        MqttPub pub = new MqttPub(Integer.toString(botDistrict), readSensor, botId);
        pub.start(); //start or run?

        //Server GRPCserver = ServerBuilder.forPort(botPort).addService(new SERVICE()).build();
        //GRPCserver.start();

        try {
            bot.initialize();
        }
        catch(Exception e) {

        }

        //clientSocket.close();
    }*/


}
