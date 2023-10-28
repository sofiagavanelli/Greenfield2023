package Utils;

import java.util.List;

public class MqttMsg {

    long timestamp;
    List<Double> averages;
    int robotID;

    public MqttMsg() {
    }

    public MqttMsg(List<Double> averages, int robotID) {
        this.timestamp = System.currentTimeMillis();
        this.averages = averages;
        this.robotID = robotID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Double> getAverages() {
        return averages;
    }

    public int getRobotID() {
        return robotID;
    }
}
