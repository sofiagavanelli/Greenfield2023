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

    /*public String getStringMessage() {
        String msg = Integer.toString(robotID);
        msg = msg.concat(":");
        msg = msg.concat(String.valueOf(averages));
        msg = msg.concat(":");
        msg = msg.concat(String.valueOf(timestamp));

        return msg;
    }*/

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
