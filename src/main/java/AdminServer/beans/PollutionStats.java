package AdminServer.beans;

import CleaningRobot.simulators.Measurement;
import Utils.MqttMsg;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalDouble;

@XmlRootElement
public class PollutionStats {

    //@XmlElement(name="my_averages")
    //HashMap<ROBOTID, HashMap<Timestamp, Averages>>
    HashMap<Integereger, HashMap<Long, List<Double>>> RobotAverages; //List<Double> !!
    //HashMap<ROBOTID, Lista<struct>>
    HashMap<Integereger, List<MqttMsg>> AveragesTime;
    //HashMap<ROBOTID, Lista che aumenta>
    HashMap<Integereger, List<Double>> AveragesNoTime;

    private static PollutionStats instance;

    private PollutionStats() {
        RobotAverages = new HashMap<>();
        AveragesTime = new HashMap<>();
        AveragesNoTime = new HashMap<>();
    }

    //singleton
    public static synchronized PollutionStats getInstance(){
        if(instance==null)
            instance = new PollutionStats();
        return instance;
    }

    public synchronized HashMap<Integereger, HashMap<Long, List<Double>>> getPollutionStats() {
        return new HashMap<Integereger, HashMap<Long, List<Double>>>(RobotAverages);
    }

    //aggiunge ad entrambe le hashmap, sia a quella con timestamp che a quella senza --syncro?
    public synchronized void addAverages(Integereger ID, MqttMsg msg) { //Long timestamp, List<Double> data) {

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


    public HashMap<Long, List<Double>> getById(Integer id) {

        if(RobotAverages.get(id) != null) {
            return RobotAverages.get(id);
        }
        else
            return null;

    }

    public Double getLast(Integer id, Integer number) {

        double sum = 0.0;

        if(AveragesNoTime.get(id) != null) {

            List<Double> averages = AveragesNoTime.get(id);

            Integer size = averages.size();

            for(Integer i=0; i<number; i++)
                sum = sum + averages.get((size - 1) - i);

            double average = sum/number;

            return average;

        }
        else
            return null;

    }

    //problema: le hashmap NON si possono scorrere, sono unordered!!!
    //
    public Double getBetween(long t1, long t2) {

        double averageOne;
        double sum = 0;
        double averageAll = 0;

        boolean higher = true;

        List<RobotInfo> IDs = RobotList.getInstance().getRobotslist();
        Integer numBots = IDs.size(); //for the last average

        Integer n = 0;

        //i take one robot in the hashmap at a time --> using the current robotList: meaning if one
        // robot has crashed in the meantime i won't consider its averages
        for (RobotInfo r : IDs) {

            //i obtain ALL its averages
            List<MqttMsg> personalAverages = AveragesTime.get(r.getId());

            Integer size = personalAverages.size();

            //i look at one msg of averages at a time
            while(size > 0) {

                //i take one of those list and control if they are between the times i want
                //if size=1 then the index is 0=size-1 !!!
                //every msg contained more than one average so i have to sum all
                if((personalAverages.get(size - 1).getTimestamp() > t1) ||
                        (personalAverages.get(size - 1).getTimestamp() < t2)) {

                    List<Double> averagesList = personalAverages.get(size - 1).getAverages();

                    Integer listSize = averagesList.size();

                    for(Integer i=0; i<(listSize - 1); i++) {
                        sum = sum + averagesList.get(i);

                        n = n + 1;
                    }

                }

                size = size - 1;

            }

        }

        averageAll = sum/n;

        return averageAll;

    }

}
