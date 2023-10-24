package CleaningRobot.breakHandler;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.simulators.PM10Simulator;

import java.util.List;
import java.util.Random;

public class Mechanic extends Thread {

    boolean stopCondition = false;
    int botId;
    int botPort;

    List<RobotInfo> listCopy;
    int[] RobotPortInfo;

    Random rnd = new Random();

    PM10Simulator botSimulator;
    Reader readSensor;

    public Mechanic(PM10Simulator botSimulator, Reader readSensor, int botId, int botPort) {
        this.botSimulator = botSimulator;
        this.readSensor = readSensor;

        this.botId = botId;
        this.botPort = botPort;
    }

    @Override
    public void run() {

        //while(!stopCondition) {
        while(robotState.getInstance().getState() == STATE.WORKING) {

            try {
                //wait for requests
                System.out.println("line 42: ciao");
                //deal with those

                //mechanic:
                sleep(10000);


                //isWorking();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //System.out.println("prova");
            //isWorking();
        }

        goToMechanic();

    }

    public void isWorking() {

        int i = rnd.nextInt(100);
        //int i = 8;

        if(i < 10 && i >= 0) {
            stopCondition = true;
            System.out.println("This robot has crashed");
        }

    }

    public void goToMechanic() {

        //ripetizione: se entra qui vuol dire che è già stato settato a needing
        //robotState.getInstance().setState(STATE.NEEDING);

        listCopy = RobotList.getInstance().getRobotslist();
        System.out.println(listCopy.toString());
        //li stampa come: [AdminServer.beans.RobotInfo@2f567cae, AdminServer.beans.RobotInfo@7847872c, AdminServer.beans.RobotInfo@76807654]

        if(! (robotState.getInstance().getState() == STATE.NEEDING)) //in case this function has been called by the admin client from cmd line
            robotState.getInstance().setState(STATE.NEEDING);

        //no stop!!
        //botSimulator.stopMeGently();
        //readSensor.stopReading();

        //slide synchro slide 18

        try {
            RobotP2P.requestMechanic(listCopy, botPort, botId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //ha ottenuto le autorizzazioni per andare dal meccanico
        robotState.getInstance().setState(STATE.MECHANIC);
        try {
            sleep(10000); //10s di meccanico
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robotState.getInstance().setState(STATE.WORKING);

        run();

    }

    public void setConnections(int[] ports) {
        this.RobotPortInfo = ports;
    }

}
