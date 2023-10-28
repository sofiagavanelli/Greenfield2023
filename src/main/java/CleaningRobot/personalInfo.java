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

    public personalInfo(int botID, int district, int x, int y, int port) {
        this.botID = botID;
        this.district = district;
        this.x = x;
        this.y = y;
        this.port = port;
    }

    public personalInfo() {

    }

    //singleton
    public static synchronized personalInfo getInstance(){
        if(instance==null)
            instance = new personalInfo();
        return instance;
    }

    public int getBotID() {
        return botID;
    }

    public void setBotID(int botID) {
        this.botID = botID;
    }


}
