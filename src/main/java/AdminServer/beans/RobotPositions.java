package AdminServer.beans;

import java.util.*;

public class RobotPositions {

    private ArrayList<Integer> districts;
    private HashMap<Integer, List<Integer>> distribution;

    private static RobotPositions instance;

    //--> il costruttore infatti è privato
    private RobotPositions() {
        districts = new ArrayList<Integer>(Collections.nCopies(4, 0));; //ne crea una nuova//
        distribution = new HashMap<>();
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

    public synchronized void addDistrict(int i) {
        districts.set(i-1, districts.get(i-1)+1);
        //districts.add(i-1, 1);
    }

    public synchronized int getDistrict(int i) {
        return(districts.get(i-1));
    }

    public synchronized HashMap<Integer, List<Integer>> getDistribution() {
        return new HashMap<>(distribution);
    }

    public synchronized void addIntoDistribution(int district, int id) {

        List<Integer> old = new ArrayList<>();

        if(distribution.get(district) != null) {
            System.out.println("sono dentro l'if");
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

    public void removeFromDistrict(int i) {
        districts.set(i-1, districts.get(i-1)-1);
    }

    public boolean newPosition(RobotInfo bot) {

        Random pos = new Random();

        //botDistricts = RobotPositions.getInstance().getRobotsDistricts();

        boolean found = false;

        int d = 0;

        //sarebbe da mettere in modalità random!!

        while(!found) {
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
            //if there isn't one with less than it means they are all equal and we can choose this
            if(!found) {
                d = d + 1;
                found = true;
            }
        }

        //we put the elements in 0=1, 1=2, 2=3, 3=4 where (position=district)
        //districts.add(d - 1, 1); //inutile?
        RobotPositions.getInstance().addDistrict(d);
        System.out.println("DISTRETTO " + d + " bot dentro: " + RobotPositions.getInstance().getDistrict(d));

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

        System.out.println("x: " + x);
        System.out.println("y: " + y);

        bot.setCoordinates(x, y, d);

        System.out.println(distribution);

        List<Integer> old = new ArrayList<>();

        if(distribution.get(d) != null) {
            System.out.println("sono dentro l'if");
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
