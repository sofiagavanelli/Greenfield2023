package AdminServer;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import AdminServer.MQTT.MqttSub;
import java.util.Locale;

public class AdminServer {

    //the server itself is a server which also subscribes to the mqtt broker
    private static final String HOST = "localhost";
    private static final int PORT = 1337;

    private static final Logger logger = Logger.getLogger(AdminServer.class.getSimpleName());

    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %3$s : %5$s %n");
        Logger.getLogger("com.sun.jersey").setLevel(Level.SEVERE);
    }

    public static void main(String[] args) throws IOException {
        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = "ADMIN-SERVER";
        String topic = "greenfield/pollution/#";

        int qos = 2;

        try{
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
            logger.severe("reason " + me.getReasonCode());
            logger.severe("msg " + me.getMessage());
            logger.severe("loc " + me.getLocalizedMessage());
            logger.severe("cause " + me.getCause());
            logger.severe("except " + me);
            me.printStackTrace();
        }



    }

}
