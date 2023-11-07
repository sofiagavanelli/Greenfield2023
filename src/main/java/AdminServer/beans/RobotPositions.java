package AdminServer.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RobotPositions {

    private ArrayList<Integer> districts;

    private static RobotPositions instance;

    //--> il costruttore infatti è privato
    private RobotPositions() {
        districts = new ArrayList<Integer>(Collections.nCopies(4, 0));; //ne crea una nuova//
        //robotsList.add(new RobotInfo(12, 7));
    }

    //singleton
    public static synchronized RobotPositions getInstance(){
        if(instance==null)
            instance = new RobotPositions();
        return instance;
    }

    public synchronized ArrayList<Integer> getRobotsDistricts() {

        return new ArrayList<>(districts);
    }

    public ArrayList<Integer> getDistricts() {
        return districts;
    }

    public void addDistricts(Integer i) {
        districts.add(i, 1);
    }

}
