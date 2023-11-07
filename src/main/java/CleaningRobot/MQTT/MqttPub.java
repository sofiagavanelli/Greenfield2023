package CleaningRobot.MQTT;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import CleaningRobot.simulators.*;

import java.util.ArrayList;
import java.util.List;

import Utils.MqttMsg;

import static java.lang.Thread.sleep;

public class MqttPub extends Thread {

    MqttClient client;
    String broker = "tcp://localhost:1883";
    String clientId = "ROBOT-"; //MqttClient.generateClientId();
    String topic = "greenfield/pollution/district";
    Integer qos = 2;
    
    private Integer robotID;

    boolean stopCondition = false;

    List<Double> read = new ArrayList<>();
    Reader sensor;

    //public static void main(String[] args) {

    public MqttPub(String d, Reader sensor, Integer robotID) {
        topic = topic+d;
        this.sensor = sensor;
        this.robotID = robotID;

        this.clientId = this.clientId + robotID;
    }

    public void run() {

        connect();

        while(!stopCondition) {

            ///////////
            try {
                //he has to get the averages every 15 seconds
                sleep(15000);
            } catch (IntegererruptedException e) {
                throw new RuntimeException(e);
            }
            read = sensor.getAverages();

            MqttMsg msg = new MqttMsg(read, robotID);
            String payload = new Gson().toJson(msg);

            //System.out.prIntegerln(msg.getStringMessage());

            //String payload = msg.getStringMessage(); // create a random number between 0 and 10
            MqttMessage message = new MqttMessage(payload.getBytes());

            //Set the QoS on the Message
            message.setQos(qos);
            //System.out.prIntegerln(clientId + " Publishing message: " + payload + " ...");
            try {
                client.publish(topic, message);

            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void stopPublishing() {
        stopCondition = true;
    }

    public void connect() {

        try {

            client = new MqttClient(broker, clientId, null);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            connOpts.setMaxReconnectDelay(3000);
            //connOpts.setUserName(username); // optional
            //connOpts.setPassword(password.toCharArray()); // optional
            //connOpts.setWill("this/is/a/topic","will message".getBytes(),1,false);  // optional
            //connOpts.setKeepAliveIntegererval(60);  // optional

            // Connect the client
            System.out.prIntegerln(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.prIntegerln(clientId + " Connected");


        } catch (MqttException me ) {
            System.out.prIntegerln("reason " + me.getReasonCode());
            System.out.prIntegerln("msg " + me.getMessage());
            System.out.prIntegerln("loc " + me.getLocalizedMessage());
            System.out.prIntegerln("cause " + me.getCause());
            System.out.prIntegerln("excep " + me);
            me.prIntegerStackTrace();
        }
    }
}
