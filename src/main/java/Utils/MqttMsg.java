package Utils;

import java.util.List;

public class MqttMsg {

    Long timestamp;
    List<Double> averages;
    int robotID;

    public MqttMsg(List<Double> averages, int robotID) {
        this.timestamp = System.currentTimeMillis();
        this.averages = averages;
        this.robotID = robotID;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public List<Double> getAverages() {
        return averages;
    }

    public int getRobotID() {
        return robotID;
    }
}
