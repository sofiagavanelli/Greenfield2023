package AdminServer;

import AdminServer.beans.PollutionStats;
import CleaningRobot.breakHandler.crashSimulator;
import Utils.MqttMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import AdminServer.MQTT.MqttSub;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.logging.Formatter;
import java.util.logging.SimpleFormatter;
import java.util.Locale;

public class AdminServer {

    //the server itself is a server which also subscribes to the mqtt broker
    private static final String HOST = "localhost";
    private static final int PORT = 1337;

    private static final Logger logger = Logger.getLogger(AdminServer.class.getSimpleName());

    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %3$s : %5$s %n");
        Logger.getLogger("com.sun.jersey").setLevel(Level.SEVERE);
    }

    public static void main(String[] args) throws IOException, MqttException {
        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = "ADMIN-SERVER";
        String topic = "greenfield/pollution/#";

        int qos = 2;

        try {
            client = new MqttClient(broker, clientId, null);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            connOpts.setMaxReconnectDelay(3000);

            // Connect the client
            logger.info(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            logger.info(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());

            // Callback
            client.setCallback(new MqttSub(clientId));

            logger.info(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
            client.subscribe(topic,qos);
            logger.info(clientId + " Subscribed to topics : " + topic);

            HttpServer server = HttpServerFactory.create("http://"+HOST+":"+PORT+"/");
            server.start();

            logger.info("Server running!");
            logger.info("Server started on: http://"+HOST+":"+PORT);


            System.in.read();


        } catch (MqttException me ) {
            logger.info("reason " + me.getReasonCode());
            logger.info("msg " + me.getMessage());
            logger.info("loc " + me.getLocalizedMessage());
            logger.info("cause " + me.getCause());
            logger.info("excep " + me);
            me.printStackTrace();
        }



    }

}
