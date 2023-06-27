package AdminServer.beans;

import Utils.MqttMsg;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@XmlRootElement
public class PollutionStats {

    //@XmlElement(name="my_averages")
    HashMap<Integer, HashMap<Long, List<Double>>> RobotAverages; //List<Double> !!
    HashMap<Integer, List<MqttMsg>> AveragesTime;

    HashMap<Integer, List<Double>> AveragesNoTime;

    private static PollutionStats instance;

    private PollutionStats() {
        RobotAverages = new HashMap<>();
        AveragesNoTime = new HashMap<>();
    }

    //singleton
    public static synchronized PollutionStats getInstance(){
        if(instance==null)
            instance = new PollutionStats();
        return instance;
    }

    public synchronized HashMap<Integer, HashMap<Long, List<Double>>> getPollutionStats() {
        return new HashMap<Integer, HashMap<Long, List<Double>>>(RobotAverages);
    }

    public void addAverages(Integer ID, MqttMsg msg) { //Long timestamp, List<Double> data) {

        //HashMap<Long, List<Double>> previous = new HashMap<>();
        List<MqttMsg> previous = new ArrayList<>();

        List<Double> copyNoTime = new ArrayList<>();

        if(RobotAverages.get(ID) != null) {
            //previous = RobotAverages.get(ID);
            previous = AveragesTime.get(ID);

            copyNoTime = AveragesNoTime.get(ID);
        }

        copyNoTime.addAll(msg.getAverages());
        AveragesNoTime.put(ID, copyNoTime);

        //previous.put(timestamp, data);
        //RobotAverages.put(ID, previous);
        previous.add(msg);
        AveragesTime.put(msg.getRobotID(), previous);

    }


    public HashMap<Long, List<Double>> getById(int id) {

        if(RobotAverages.get(id) != null) {
            return RobotAverages.get(id);
        }
        else
            return null;

    }

    public Double getLast(int id, int number) {

        double sum = 0.0;

        if(AveragesNoTime.get(id) != null) {

            List<Double> averages = AveragesNoTime.get(id);

            int size = averages.size();

            for(int i=0; i<number; i++)
                sum = sum + averages.get((size - 1) - i);

            double average = sum/number;

            return average;

        }
        else
            return null;

    }

    /*public Double getBetween(long t1, long t2) {

        double averageOne;
        double sum;
        double averageAll;

        boolean higher = true;

        List<RobotInfo> IDs = RobotList.getInstance().getRobotslist();
        int numBots = IDs.size(); //for the last average

        for (RobotInfo element : IDs) {

            while (higher) {

            }

        }


    }*/

}
