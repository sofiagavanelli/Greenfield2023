package CleaningRobot.breakHandler;

import AdminServer.beans.RobotList;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.simulators.PM10Simulator;
import Utils.RestFunc;

import java.util.List;
import java.util.Random;

public class crashSimulator extends Thread {

    boolean stopCondition = false;

    static Object crash = new Object();

    Random rnd = new Random();

    @Override
    public void run() {

        while(!stopCondition) {

            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Integer i = rnd.nextInt(100);
            //Integer i = 8;

            if (i < 10 && i >= 0) {
                //stopCondition = true;

                //non stoppando niente devo cambiare lo stato solo quando è working, se è già needing non faccio nulla
                if(robotState.getInstance().getState() == STATE.WORKING) {
                    signalCrash();
                }
            }

        }

    }

    public void stopCrash() {
        stopCondition = true;
    }

    public static void waitingForCrash() {
        synchronized (crash) {
            while(robotState.getInstance().getState() == STATE.WORKING) {
                try {
                    crash.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void signalCrash() {
        synchronized (crash) {
            robotState.getInstance().setState(STATE.NEEDING);
            crash.notify();
            System.out.println("This robot has crashed");
        }
    }


    public static void dealUncontrolledCrash(Integer id) {

        //new client ?????
        //e quello già aperto su robot ??
        RestFunc.deleteRobot(id);
        RestFunc.requestDistricts();

        try {
            RobotP2P.organize(id);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}