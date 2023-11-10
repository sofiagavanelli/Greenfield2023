package CleaningRobot.breakHandler;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import AdminServer.beans.RobotPositions;
import CleaningRobot.MQTT.MqttPub;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.simulators.PM10Simulator;
import Utils.RestFunc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class crashSimulator extends Thread {

    boolean stopCondition = false;

    private static final Logger logger = Logger.getLogger(crashSimulator.class.getSimpleName());

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

            logger.info("This robot has crashed");
        }
    }


    public static HashMap<Integer, Integer> dealUncontrolledCrash(int id) {

        System.out.println("inside dealUncontrolledCrash");

        //in this remove there is also the removeFromDistribution
        RobotList.getInstance().remove(id);

        boolean balanced = false;

        HashMap<Integer, List<Integer>> distribution = RobotPositions.getInstance().getDistribution();
        int n = RobotList.getInstance().getRobotslist().size()/4;

        List<Integer> move = new ArrayList<>();
        List<Integer> need = new ArrayList<>();

        //hashmap<id, newDistrict>
        HashMap<Integer, Integer> changes = new HashMap<>();

        System.out.println("what i have: ");
        System.out.println(distribution);

        for(int i=1; i<=4; i++) {
            if(distribution.get(i) != null && distribution.get(i).size() < n) {
                need.add(i);
            } else if (distribution.get(i) != null && distribution.get(i).size() > n) {
                move.add(i);
            }
            //it is empty
            if (distribution.get(i) == null) {
                need.add(i);
            }
        }


        if((move.size() > 0) && (need.size() > 0)) {
            int minSize = (Math.min(move.size(), need.size()));

            for (int i = 0; i < minSize; i++) {
                changes.put(distribution.get(move.get(i)).get(i), need.get(i));
            }
        }

        System.out.println("changes is: " + changes);
        /*if(need != null)
            System.out.println("need is: " + need);*/

        System.out.println("se non ci sono stampe e' perche' siamo pari");
        System.out.println("robot " + distribution.get(move.get(0)).get(0) + " should move from district " + move.get(0) + " to district " + need.get(0));

        //i'm one of the robot who has to change
        int myId = RobotInfo.getInstance().getId();
        if(changes.get(myId) != null) {
            RobotInfo.getInstance().setDistrict(changes.get(myId));
            System.out.println("my topic before changes is: ");
            MqttPub.changeTopic(changes.get(myId));
            System.out.println("after is: ");
        }


        return changes;

    }


}