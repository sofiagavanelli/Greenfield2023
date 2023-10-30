package CleaningRobot;

import CleaningRobot.gRPC.MechanicRequests;
import com.example.chat.CommunicationServiceOuterClass;

import java.util.ArrayList;

public class personalInfo {

    private int botID;
    private int district;

    private int x;
    private int y;

    private int port;

    private static personalInfo instance;

    public personalInfo() {

    }

    //singleton
    public static synchronized personalInfo getInstance(){
        if(instance==null)
            instance = new personalInfo();
        return instance;
    }

    public void setAll(int botID, int district, int x, int y, int port) {
        this.botID = botID;
        this.district = district;
        this.x = x;
        this.y = y;
        this.port = port;
    }

    public int getBotID() {
        return botID;
    }

    public int getPort() {
        return port;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setBotID(int botID) {
        this.botID = botID;
    }


}
