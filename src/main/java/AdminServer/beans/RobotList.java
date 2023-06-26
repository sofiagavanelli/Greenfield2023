package AdminServer.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.sound.midi.SysexMessage;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class RobotList {

    @XmlElement(name="my_robots")
    private List<RobotInfo> robotsList;

    //singleton --> pattern usato per avere solo UNA instanza di questa lista
    private static RobotList instance;


    //--> il costruttore infatti è privato
    private RobotList() {
        robotsList = new ArrayList<RobotInfo>(); //ne crea una nuova//
        //robotsList.add(new RobotInfo(12, 7));
    }

    //singleton
    public static synchronized RobotList getInstance(){
        if(instance==null)
            instance = new RobotList();
        return instance;
    }

    public synchronized List<RobotInfo> getRobotslist() {
        return new ArrayList<>(robotsList);
    }

    public synchronized void setRobotslist(List<RobotInfo> list) {
        robotsList = list;
    }


    public boolean add(RobotInfo bot) {

        List<RobotInfo> listCopy = getRobotslist();

        for(RobotInfo r : listCopy) {
            if (r.getId() == (bot.getId())) {
                return false;
            }
        }

        robotsList.add(bot);

        /*then you should send back:
            • the starting position in Greenfield of the robot
            • the list of robots already located in the smart city, specifying for
                each of them the related ID, the IP address, and the port number
                for communication*/

        return true;

    }



}
