package CleaningRobot;

import AdminServer.AdminServer;
import AdminServer.beans.RobotInfo;
import CleaningRobot.MQTT.MqttPub;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.breakHandler.Mechanic;
import CleaningRobot.breakHandler.STATE;
import CleaningRobot.breakHandler.crashSimulator;
import CleaningRobot.breakHandler.robotState;
import CleaningRobot.gRPC.CommunicationService;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.simulators.PM10Simulator;
import CleaningRobot.simulators.WindowBuffer;
import Utils.RestFunc;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;

public class Robot {

    private static int botId;
    private static int botPort;
    static boolean stopCondition = false;

    WindowBuffer newB;
    Reader readSensor;
    PM10Simulator botSimulator;
    Mechanic mechanicHandler;
    crashSimulator crashTest;
    Server gRPCserver;
    MqttPub pub;

    private static final Logger logger = Logger.getLogger(AdminServer.class.getSimpleName());


    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %3$s : %5$s %n");
    }

    public Robot() {

    }

    void initialize() {

        WindowBuffer newB = new WindowBuffer(8);
        this.newB = newB;
        this.botSimulator = new PM10Simulator(newB);
        Reader readSensor = new Reader(newB);
        this.readSensor = readSensor;
        this.mechanicHandler = new Mechanic();
        this.crashTest = new crashSimulator();

        //MqttPub : ogni robot ha il suo publisher
        this.pub = new MqttPub(Integer.toString(RobotInfo.getInstance().getDistrict()), readSensor, botId);
        this.pub.start();

        this.startGRPCServer();

        try {
            RobotP2P.firstMSG();
        }  catch (InterruptedException e) {
           logger.severe("Problems sending first message");
        }

        this.botSimulator.start();
        this.readSensor.start();
        this.crashTest.start();
        this.mechanicHandler.start();

    }

    public void stop() throws InterruptedException {

        //not failing anymore
        crashTest.stopCrash();
        crashTest.join();
        if(robotState.getInstance().getState() == STATE.WORKING)
            crashSimulator.signalCrash();
        mechanicHandler.stopMechanic();
        mechanicHandler.join();

        logger.warning("Going to stop the other threads: there is a sleep somewhere, it can take some time.");

        botSimulator.stopMeGently();
        botSimulator.join();
        pub.stopPublishing();
        pub.join();
        readSensor.stopReading();
        //devo usare l'interrupt perché dentro il reader si chiama una funzione del simulator che ha dentro una wait
        readSensor.interrupt();
        readSensor.join();

        logger.info("Saying goodbye to the others.");

        try {
            RobotP2P.lastMSG();
        } catch (InterruptedException e) {
            logger.severe("Last message never sent");
        }

        //make the server delete me
        RestFunc.deleteRobot(RobotInfo.getInstance().getId());

        gRPCserver.shutdown();

        stopCondition = true;

    }

    private void startGRPCServer() {

        gRPCserver = ServerBuilder.forPort(botPort)
                .addService(new CommunicationService())
                .build();

        try {
            gRPCserver.start();
        }
        catch (Exception e) {
            logger.severe("The GRPC server couldn't start");
        }
    }

    public static void main(String[] argv) throws Exception {

        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        System.out.print("Enter id: ");
        botId = sc.nextInt();
        System.out.print("Enter port: ");
        botPort = sc.nextInt();

        boolean added;

        Robot bot = new Robot();
        added = RestFunc.addNewRobot(botId, botPort);

        while(!added){
            System.out.println("You inserted an ID/port already in use.");
            System.out.print("Enter new id: ");
            botId = sc.nextInt();
            System.out.print("Enter new port: ");
            botPort = sc.nextInt();

            added = RestFunc.addNewRobot(botId, botPort);
        }

        bot.initialize();

        while(!stopCondition) {

            String cmd = sc.nextLine();

            if(cmd.equalsIgnoreCase("fix"))
                crashSimulator.signalCrash();
            else if (cmd.equalsIgnoreCase("quit"))
                bot.stop();
        }

        System.exit(0);

    }


}
