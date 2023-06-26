package AdminServer.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@XmlRootElement
public class PollutionStats {

    //@XmlElement(name="my_averages")
    HashMap<Integer, HashMap<Long, List<Double>>> RobotAverages; //List<Double> !!

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

    public void addAverages(Integer ID, Long timestamp, List<Double> data) {

        HashMap<Long, List<Double>> previous = new HashMap<>();

        List<Double> copyNoTime = new ArrayList<>();

        if(RobotAverages.get(ID) != null) {
            previous = RobotAverages.get(ID);

            copyNoTime = AveragesNoTime.get(ID);
        }

        copyNoTime.addAll(data);
        AveragesNoTime.put(ID, copyNoTime);

        previous.put(timestamp, data);
        RobotAverages.put(ID, previous);

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

}
