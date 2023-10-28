package CleaningRobot;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.MQTT.MqttPub;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.breakHandler.Mechanic;
import CleaningRobot.breakHandler.crashSimulator;
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

    private static int botId;
    private static int botPort;
    private static int botDistrict;
    private static int x;
    private static int y;

    static Random rnd = new Random();

    List<RobotInfo> robotInfoList;
    int[] RobotPortInfo;

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

    public Robot(int botId, int botPort, PM10Simulator botSimulator, Reader readSensor, WindowBuffer newB, Mechanic mechanicHandler, crashSimulator crashTest/*, RobotP2P gRPCclient*/) {
        this.botId = botId;
        this.botPort = botPort;

        this.botSimulator = botSimulator;
        this.readSensor = readSensor;
        this.newB = newB;

        this.mechanicHandler = mechanicHandler;
        this.crashTest = crashTest;
        //this.gRPCclient = gRPCclient;
    }

    void initialize() {

        //this.gRPCclient.start();
        this.startGRPCServer();

        try {
            List<RobotInfo> list = RobotList.getInstance().getRobotslist();
            //int[] RobotPortInfo, int botPort, int botDistrict, int botID
            RobotP2P.firstMSG(list, botPort, botDistrict, botId, x, y);
        }  catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.botSimulator.start();
        this.readSensor.start();

        this.mechanicHandler.setConnections(RobotPortInfo);

        this.crashTest.start();

        this.mechanicHandler.start();

        //System.out.println("sono a riga 73 di initialize");

    }

    public static void main(String argv[]) throws Exception {

        Scanner sc= new Scanner(System.in);    //System.in is a standard input stream
        System.out.print("Enter id: ");
        botId = sc.nextInt();
        System.out.print("Enter port: ");
        botPort = sc.nextInt();

		/* client socket initialization
			localhost: server address
			6789: server service port number */
        //Socket clientSocket = new Socket("localhost", 6789);

        WindowBuffer newB = new WindowBuffer(8);
        PM10Simulator botSimulator = new PM10Simulator(newB);
        Reader readSensor = new Reader(newB);
        Mechanic mechanicHandler = new Mechanic(botSimulator, readSensor, botId, botPort);
        crashSimulator crashTest = new crashSimulator();
        //RobotP2P gRPCclient = new RobotP2P(botId, botPort);

        botDistrict = 0; //non dovrebbe servire
        //cambiare botId in String? e mettere "ROBOT-n" ?
        //botId = rnd.nextInt(100) + 10;
        //botPort = 1234; //input??

        //RobotP2P gRPCclient = new RobotP2P(botId, botPort);
        Robot bot = new Robot(botId, botPort, botSimulator, readSensor, newB, mechanicHandler, crashTest/*, gRPCclient*/);

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

        personalInfo robot = new personalInfo(botId, botDistrict, x, y, botPort);

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


}
