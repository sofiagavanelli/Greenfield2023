package AdminServer.beans;

import java.util.*;

public class RobotPositions {

    //the index of the array is the district and the integer is the number of robots
    private final ArrayList<Integer> districts;
    //hashmap<district, list of robot's ID>
    private final HashMap<Integer, List<Integer>> distribution;

    private static RobotPositions instance;

    //--> il costruttore infatti è privato
    private RobotPositions() {
        districts = new ArrayList<Integer>(Collections.nCopies(4, 0));
        distribution = new HashMap<>();
    }

    //singleton
    public static synchronized RobotPositions getInstance(){
        if(instance==null)
            instance = new RobotPositions();
        return instance;
    }

    //ROBOTS DISTRICT
    public synchronized ArrayList<Integer> getRobotsDistricts() {
        return new ArrayList<>(districts);
    }

    public synchronized void addDistrict(int i) {
        districts.set(i-1, districts.get(i-1)+1);
        //districts.add(i-1, 1);
    }

    public void removeFromDistrict(int i) {
        districts.set(i-1, districts.get(i-1)-1);
    }

    public synchronized int getDistrict(int i) {
        return(districts.get(i-1));
    }

    //ROBOTS DISTRIBUTION
    public synchronized HashMap<Integer, List<Integer>> getDistribution() {
        return new HashMap<>(distribution);
    }

    public synchronized void addIntoDistribution(int district, int id) {

        List<Integer> old = new ArrayList<>();

        if(distribution.get(district) != null) {
            //i get the list of the robots already in the district d
            old = distribution.get(district);
            //i put this new robot
            old.add(id);
        }
        else {
            //there aren't robot in distribution(d) so i need to add the new one
            old.add(id);
        }
        distribution.put(district, old);

    }

    public void removeFromDistribution(int district, int id) {

        List<Integer> old = distribution.get(district);
        if(old.size() == 1)
            //if there is only one element then the district will be empty
            distribution.remove(district);
        else if(old.size() > 1) {
            old.remove((Integer) id);
            distribution.put(district, old);
        }

    }

    public boolean newPosition(RobotInfo bot) {

        Random pos = new Random();

        boolean found = false;

        int d = 0;

        d = pos.nextInt(4);
        //if this district is empty then it's okay
        if(districts.get(d) == 0) {
            d = d + 1;
            found = true;
        }
        else {
            //if the first is not empty then i look for one with less than this
            for(int i=0; i<4; i++) {
                if (districts.get(i) < districts.get(d)) {
                    d = i + 1;
                    found = true;
                }
            }
        }
        //if i still haven't found it then
        // there isn't one with less than the others and it means they are all equal and we can choose this
        if(!found) {
            d = d + 1;
        }

        //we put the elements in 0=1, 1=2, 2=3, 3=4 where (position=district-1)
        addDistrict(d);

        int x = pos.nextInt(5);
        int y = pos.nextInt(5);

        if(d == 2) {
            x = x + 5;
        }
        else if(d == 3) {
            x = x + 5;
            y = y + 5;
        }
        else if(d == 4) {
            y = y + 5;
        }

        bot.setCoordinates(x, y, d);

        List<Integer> old = new ArrayList<>();

        if(distribution.get(d) != null) {
            //i get the list of the robots already in the district d
            old = distribution.get(d);
            //i put this new robot
            old.add(bot.getId());
        }
        else {
            //there aren't robot in distribution(d) so i need to add the new one
            old.add(bot.getId());
        }
        distribution.put(d, old);

        return true;

    }

}
