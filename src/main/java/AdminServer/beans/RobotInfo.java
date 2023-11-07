package AdminServer.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RobotInfo {

    private Integer id;
    private Integer portN;
    Integer x; //Integer x Integer y separated?
    Integer y;
    Integer district;
    //IPaddress? String?
    String ipAddress;

    private static RobotInfo instance;

    public RobotInfo() {}

    //equivalent of @post robot
    public RobotInfo(Integer id, Integer portN) { //, String ipAddress) {
        this.id = id;
        this.portN = portN;
        //this.ipAddress = ipAddress;
    }

    public RobotInfo(Integer id, Integer portN, Integer x, Integer y, Integer district) {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setPortN(Integer p) { this.portN = p; }

    public Integer getPortN() {
        return portN;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getX() {
        return x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getY() {
        return y;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setCoordinates(Integer posX, Integer posY, Integer d){
        x = posX;
        y = posY;
        district = d;
    }

    public void setAll(Integer id, Integer district, Integer x, Integer y, Integer portN) {
        this.id = id;
        this.district = district;
        this.x = x;
        this.y = y;
        this.portN = portN;
    }

}