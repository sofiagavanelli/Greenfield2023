package CleaningRobot;

import AdminServer.beans.RobotInfo;
import CleaningRobot.MQTT.MqttPub;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.breakHandler.Mechanic;
import CleaningRobot.breakHandler.crashSimulator;
import CleaningRobot.gRPC.CommunicationService;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.simulators.PM10Simulator;
import CleaningRobot.simulators.WindowBuffer;
import Utils.RestFunc;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Robot {

    private static int botId;
    private static int botPort;

    //static
    WindowBuffer newB; // = new WindowBuffer(8);
    //static
    Reader readSensor; // = new Reader(newB);
    //static
    PM10Simulator botSimulator; // = new PM10Simulator(newB);
    Mechanic mechanicHandler;
    crashSimulator crashTest;
    Server gRPCserver;
    MqttPub pub;

    ClientConfig config = new DefaultClientConfig();

    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %3$s : %5$s %n");
    }

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

        RestFunc.addNewRobot(botId, botPort);

        //MqttPub : ogni robot ha il suo publisher
        MqttPub pub = new MqttPub(Integer.toString(RobotInfo.getInstance().getDistrict()), readSensor, botId);
        this.pub = pub;
        this.pub.start(); //start or run?

        //this.gRPCclient.start();
        this.startGRPCServer();

        try {
            RobotP2P.firstMSG();
        }  catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.botSimulator.start();
        this.readSensor.start();
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

        //do i wait?
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


}
