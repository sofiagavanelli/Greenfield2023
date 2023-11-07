package AdminServer;

import AdminServer.beans.PollutionStats;
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

public class AdminServer {

    //the server itself is a server which also subscribes to the mqtt broker
    private static final String HOST = "localhost";
    private static final Integer PORT = 1337;

    static {
        Logger.getLogger("com.sun.jersey").setLevel(Level.SEVERE);
    }

    public static void main(String[] args) throws IOException, MqttException {
        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = "ADMIN-SERVER"; //MqttClient.generateClientId();
        String topic = "greenfield/pollution/#";

        Integer qos = 2;

        try {
            client = new MqttClient(broker, clientId, null);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            connOpts.setMaxReconnectDelay(3000);

            // Connect the client
            System.out.prIntegerln(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.prIntegerln(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());

            // Callback
            client.setCallback(new MqttSub(clientId));

            System.out.prIntegerln(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
            client.subscribe(topic,qos);
            System.out.prIntegerln(clientId + " Subscribed to topics : " + topic);

            //http server
            System.out.prIntegerln("I'm here'!");
            HttpServer server = HttpServerFactory.create("http://"+HOST+":"+PORT+"/");
            server.start();

            System.out.prIntegerln("Server running!");
            System.out.prIntegerln("Server started on: http://"+HOST+":"+PORT);

            //input ? to keep it alive

            System.out.prIntegerln("Hit return to stop...");
            System.in.read();
            //System.out.prIntegerln("Stopping server");
            //server.stop(0);
            //System.out.prIntegerln("Server stopped");



            //System.out.prIntegerln("\n ***  Press a random key to exit *** \n");
            /*Scanner command = new Scanner(System.in);
            command.nextLine();
            client.disconnect();*/

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
