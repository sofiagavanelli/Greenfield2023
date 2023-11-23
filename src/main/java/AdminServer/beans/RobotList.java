package AdminServer.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class RobotList {

    @XmlElement(name="my_robots")
    private final List<RobotInfo> robotsList;

    //singleton --> pattern usato per avere solo UNA instanza di questa lista
    private static RobotList instance;


    //--> il costruttore infatti è privato
    private RobotList() {
        robotsList = new ArrayList<RobotInfo>(); //ne crea una nuova//
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
        robotsList.addAll(list);
    }


    public boolean add(RobotInfo bot) {

        List<RobotInfo> listCopy = getRobotslist();

        for(RobotInfo r : listCopy) {
            if ((r.getId() == bot.getId()) || (r.getPortN() == bot.getPortN())) {
                return false;
            }
        }

        synchronized(robotsList) {
            robotsList.add(bot);
        }

        return true;

    }

    public boolean remove(int robotID) {

        List<RobotInfo> listCopy = getRobotslist();

        for(RobotInfo r : listCopy) {
            if (r.getId() == (robotID)) {
                synchronized(robotsList) {
                    robotsList.removeIf(bot -> bot.getId() == robotID);
                }
                //va anche tolto dalle posizioni !!!
                RobotPositions.getInstance().removeFromDistrict(r.getDistrict());
                RobotPositions.getInstance().removeFromDistribution(r.getDistrict(), r.getId());

                return true;
            }
        }

        return false;

    }


    public boolean isPresent(int id) {

        List<RobotInfo> copy = getRobotslist();

        for(RobotInfo r : copy) {
            if (r.getId() == (id)) {
                return true;
            }
        }

        return false;
    }


    public int getPortById(int id) {

        List<RobotInfo> copy = getRobotslist();

        for(RobotInfo r : copy) {
            if(r.getId() == id)
                return r.getPortN();
        }

        return error;
    }
}
