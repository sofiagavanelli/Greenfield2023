package CleaningRobot.MQTT;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import Utils.MqttMsg;

public class MqttPub extends Thread {

    MqttClient client;
    String broker = "tcp://localhost:1883";
    String clientId = "ROBOT-";
    static String topic = "greenfield/pollution/district";
    int qos = 2;

    private static final Logger logger = Logger.getLogger(MqttPub.class.getSimpleName());

    private int robotID;

    boolean stopCondition = false;

    List<Double> read = new ArrayList<>();
    Reader sensor;

    public MqttPub(String d, Reader sensor, int robotID) {
        topic = topic+d;
        this.sensor = sensor;
        this.robotID = robotID;

        this.clientId = this.clientId + robotID;
    }

    public void run() {

        connect();

        while(!stopCondition) {

            try {
                //he has to get the averages every 15 seconds
                sleep(15000);
            } catch (InterruptedException e) {
                logger.severe("Exception during publisher's sleep");
            }
            read = sensor.getAverages();

            MqttMsg msg = new MqttMsg(read, robotID);
            String payload = new Gson().toJson(msg);

            MqttMessage message = new MqttMessage(payload.getBytes());

            //Set the QoS on the Message
            message.setQos(qos);

            try {
                client.publish(topic, message);
            } catch (MqttException e) {
                logger.severe("Problems with publishing");
            }
        }

    }

    public static void changeTopic(int district) {
        topic = "greenfield/pollution/district"+district;
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

            // Connect the client
            logger.info(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            logger.info(clientId + " Connected");

        } catch (MqttException me) {
            logger.severe("reason " + me.getReasonCode());
            logger.severe("msg " + me.getMessage());
            logger.severe("loc " + me.getLocalizedMessage());
            logger.severe("cause " + me.getCause());
            logger.severe("except " + me);
            me.printStackTrace();
        }
    }
}
