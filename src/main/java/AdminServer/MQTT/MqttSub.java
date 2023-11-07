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
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.prIntegerln(clientId + " Connectionlost! cause:" + cause.getMessage() + ""
                + "-  Thread PID: " + Thread.currentThread().getId());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {

        //System.out.prIntegerln("inside messageArrived callback");

        // Called when a message arrives from the server that matches any subscription made by the client
        TypeToken<MqttMsg> token = new TypeToken<MqttMsg>(){};
        String receivedMessage = new String(message.getPayload());

        MqttMsg msg = new Gson().fromJson(receivedMessage, token.getType());

        PollutionStats.getInstance().addAverages(msg.getRobotID(), msg);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {};

}
