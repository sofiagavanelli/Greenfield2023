package Utils;

import java.util.List;

public class MqttMsg {

    long timestamp;
    List<Double> averages;
    Integer robotID;

    public MqttMsg() {
    }

    public MqttMsg(List<Double> averages, Integer robotID) {
        this.timestamp = System.currentTimeMillis(); //qui ci andrà il clock
        this.averages = averages;
        this.robotID = robotID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Double> getAverages() {
        return averages;
    }

    public Integer getRobotID() {
        return robotID;
    }
}
