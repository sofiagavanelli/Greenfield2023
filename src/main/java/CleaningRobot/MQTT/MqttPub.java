package CleaningRobot.MQTT;

import CleaningRobot.breakHandler.Mechanic;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import CleaningRobot.simulators.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import Utils.MqttMsg;

import static java.lang.Thread.sleep;

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

    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %3$s : %5$s %n");
    }

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
                throw new RuntimeException(e);
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
                throw new RuntimeException(e);
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
            logger.info("reason " + me.getReasonCode());
            logger.info("msg " + me.getMessage());
            logger.info("loc " + me.getLocalizedMessage());
            logger.info("cause " + me.getCause());
            logger.info("excep " + me);
            me.printStackTrace();
        }
    }
}
