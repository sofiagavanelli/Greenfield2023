package AdminServer.MQTT;

import AdminServer.beans.PollutionStats;

import Utils.MqttMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MqttSub implements MqttCallback {

    String clientId;

    PollutionStats dummy;

    String msgReceived;

    public MqttSub(String clientId) {
        this.clientId = clientId;

        this.dummy = PollutionStats.getInstance();
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println(clientId + " Connectionlost! cause:" + cause.getMessage()+ "-  Thread PID: " + Thread.currentThread().getId());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        // Called when a message arrives from the server that matches any subscription made by the client
        TypeToken<MqttMsg> token = new TypeToken<MqttMsg>(){};
        String receivedMessage = new String(message.getPayload());

        MqttMsg msg = new Gson().fromJson(receivedMessage, token.getType());

        //System.out.println(msg.getTimestamp());
        //System.out.println(msg.getRobotID());
        //System.out.println(msg.getAverages());

        dummy.addAverages(msg.getRobotID(), msg.getTimestamp(), msg.getAverages());

        /*System.out.println(clientId +" Received a Message! - Callback - Thread PID: " + Thread.currentThread().getId() +
                "\n\tTopic: " + topic +
                "\n\tRobot: " + prova[0] +
                "\n\tAverages: " + prova[1] +
                "\n\tTime of Computation: " + prova[2] +
                "\n\tQoS: " + message.getQos() + "\n");

        System.out.println("\n ***  Press a random key to exit *** \n");*/
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {};

}
