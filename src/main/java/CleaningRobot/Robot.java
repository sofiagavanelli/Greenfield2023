package CleaningRobot;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.MQTT.MqttPub;
import CleaningRobot.MQTT.Reader;
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
    Mechanic crashTest;
    RobotP2P gRPCclient;
    Server gRPCserver;

    public Robot(int botId, int botPort, PM10Simulator botSimulator, Reader readSensor, WindowBuffer newB, Mechanic crashTest/*, RobotP2P gRPCclient*/) {
        this.botId = botId;
        this.botPort = botPort;

        this.botSimulator = botSimulator;
        this.readSensor = readSensor;
        this.newB = newB;

        this.crashTest = crashTest;
        //this.gRPCclient = gRPCclient;
    }

    void initialize() {

        //this.gRPCclient.start();
        this.startGRPCServer();

        /*gRPCclient.setBots(robotInfoList);*/

        try {
            //int[] RobotPortInfo, int botPort, int botDistrict, int botID
            RobotP2P.firstMSG(RobotPortInfo, botPort, botDistrict, botId);
        }  catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.botSimulator.start();
        this.readSensor.start();

        this.crashTest.setConnections(RobotPortInfo);
        this.crashTest.start();

        //System.out.println("sono a riga 73 di initialize");

    }

    public static void main(String argv[]) throws Exception {

		/* client socket initialization
			localhost: server address
			6789: server service port number */
        //Socket clientSocket = new Socket("localhost", 6789);

        WindowBuffer newB = new WindowBuffer(8);
        PM10Simulator botSimulator = new PM10Simulator(newB);
        Reader readSensor = new Reader(newB);
        Mechanic crashTest = new Mechanic(botSimulator, readSensor, botId, botPort);
        //RobotP2P gRPCclient = new RobotP2P(botId, botPort);

        botDistrict = 0;
        //cambiare botId in String? e mettere "ROBOT-n" ?
        botId = rnd.nextInt(100) + 10;
        botPort = 1234; //input??

        //RobotP2P gRPCclient = new RobotP2P(botId, botPort);
        Robot bot = new Robot(botId, botPort, botSimulator, readSensor, newB, crashTest/*, gRPCclient*/);

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

        RobotList robs = clientResponse.getEntity(RobotList.class);
        List<RobotInfo> copyRobs = robs.getRobotslist();
        RobotList.getInstance().setRobotslist(copyRobs);
        //gRPCclient.setBots(copyRobs);

        bot.setConnections(copyRobs);

        //then you should receive back the position and the district !!!!
        for (RobotInfo r : robs.getRobotslist()){
            if(r.getId() == botId) {
                botDistrict = r.getDistrict();
                x = r.getX();
                y = r.getY();
            }
        }

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

    private void setConnections(List<RobotInfo> bots) {

        //ci dovrebbe SEMPRE essere almeno bot=me stesso
        //this.bots = bots;
        System.out.println(bots.size());
        int size = bots.size();
        RobotPortInfo = new int[size];

        System.out.println("line 45 p2p");
        System.out.println(bots.get(0).getPortN());

        int i = 0;
        //boolean stopCondition = false;

        while (size > i) { //(!stopCondition) {

            this.RobotPortInfo[i] = bots.get(i).getPortN();
            System.out.println(bots.get(i).getPortN());
            i++;
            //} else System.out.println("sono nell'else");
            //stopCondition = true; //finished elements
        }

        //}*/

    }

    /*void sendPort(RobotInfo r) {

        this.robotInfoList.add(r);

        //this.gRPCclient.setBot(r);
    }*/


}
