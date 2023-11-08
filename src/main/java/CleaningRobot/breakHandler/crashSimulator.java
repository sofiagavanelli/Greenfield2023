package CleaningRobot.breakHandler;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.simulators.PM10Simulator;
import Utils.RestFunc;

import java.util.ArrayList;
import java.util.HashMap;
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

            int i = rnd.nextInt(100);
            //int i = 8;

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


    public static void dealUncontrolledCrash(int id) {

        //new client ?????
        //e quello già aperto su robot ??
        RestFunc.deleteRobot(id);
        RestFunc.requestDistricts();

        List<RobotInfo> list = RobotList.getInstance().getRobotslist();

        HashMap<Integer, List<Integer>> distribution = new HashMap<>();
        ArrayList<Integer> districts = new ArrayList<>();
        List<Integer> move = new ArrayList<>();
        List<Integer> need = new ArrayList<>();

        HashMap<Integer, Integer> newDistribution = new HashMap<>();

        int n = list.size()/4;

        for(RobotInfo r : list) {
            List<Integer> previous = distribution.get(r.getDistrict());
            previous.add(r.getId());
            distribution.put(r.getDistrict(), previous);
            districts.add(r.getDistrict(), 1);
        }

        for(int i=0; i<4; i++) {
            if(districts.get(i) > n)
                move.add(distribution.get(i).get(0));
            else if(districts.get(i) < n)
                need.add(i);
        }

        System.out.println("The robots were: " + distribution);
        System.out.println("move is: " + move);
        System.out.println("need is: " + need);

        try {
            RobotP2P.organize(id);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}