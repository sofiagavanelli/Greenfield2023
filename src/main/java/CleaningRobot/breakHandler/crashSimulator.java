package CleaningRobot.breakHandler;

import AdminServer.beans.RobotList;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.simulators.PM10Simulator;

import java.util.List;
import java.util.Random;

public class crashSimulator extends Thread {

    boolean stopCondition = false;

    Random rnd = new Random();

    @Override
    public void run() {

        while(!stopCondition) {

            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int i = rnd.nextInt(100);
            //int i = 8;

            if (i < 10 && i >= 0) {
                //stopCondition = true;

                //non stoppando niente devo cambiare lo stato solo quando è working, se è già needing non faccio nulla
                if(robotState.getInstance().getState() == STATE.WORKING) {
                    robotState.getInstance().setState(STATE.NEEDING);
                    System.out.println("This robot has crashed");
                }
            }

        }

    }

    public void stopCrash() {
        stopCondition = true;
    }


}