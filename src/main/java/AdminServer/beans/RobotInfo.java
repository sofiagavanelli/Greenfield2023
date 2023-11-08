package AdminServer.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RobotInfo {

    private int id;
    private int portN;
    int x; //int x int y separated?
    int y;
    int district;
    //IPaddress? String?
    String ipAddress;

    private static RobotInfo instance;

    public RobotInfo() {}

    //equivalent of @post robot
    public RobotInfo(int id, int portN) { //, String ipAddress) {
        this.id = id;
        this.portN = portN;
        //this.ipAddress = ipAddress;
    }

    public RobotInfo(int id, int portN, int x, int y, int district) {
        this.id = id;
        this.portN = portN;
        this.x = x;
        this.y = y;
        this.district = district;
    }

    //getInstance !!!
    //singleton
    public static synchronized RobotInfo getInstance(){
        if(instance==null)
            instance = new RobotInfo();
        return instance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPortN(int p) { this.portN = p; }

    public int getPortN() {
        return portN;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getDistrict() {
        return district;
    }

    public void setCoordinates(int posX, int posY, int d){
        x = posX;
        y = posY;
        district = d;
    }

    public void setAll(int id, int district, int x, int y, int portN) {
        this.id = id;
        this.district = district;
        this.x = x;
        this.y = y;
        this.portN = portN;
    }

}