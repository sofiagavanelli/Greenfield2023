package AdminClient.REST;

import AdminServer.AdminServer;
import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import Utils.RestFunc;
import com.sun.jersey.api.client.ClientResponse;

import com.sun.jersey.api.client.Client;

import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminClient {

    static Client client;
    static String serverAddress;
    static ClientResponse clientResponse;

    public static void main(String[] args) {

        client = Client.create();
        serverAddress = "http://localhost:1337";
        clientResponse = null;

        boolean stopCondition = false;

        int cmd;

        //System.in is a standard input stream
        Scanner sc = new Scanner(System.in);

        while (!stopCondition) {
            System.out.println("Enter 1 to obtain the list of all the robots, 2 to calculate the average of n measurements of a specific robot and 3 to have averages between timestamps.");
            System.out.print("Your request: ");
            cmd = sc.nextInt();

            switch (cmd) {
                case 1:
                    getAllRobots();
                    break;

                case 2:
                    System.out.print("Enter robot id: ");
                    int id = sc.nextInt();
                    System.out.print("Enter number of measurements: ");
                    int n = sc.nextInt();

                    requestLastAverages(id, n);
                    break;

                case 3:
                    System.out.println("The current timestamp is: " + System.currentTimeMillis());

                    System.out.print("Enter t1: ");
                    long t1 = sc.nextLong();
                    System.out.print("Enter t2: ");
                    long t2 = sc.nextLong();
                    requestAveragesBetweenTime(t1, t2);
                    break;

                default:
                    System.out.println("There aren't requests with this number");

            }

        }

    }


        public static void requestLastAverages(int id, int n) {

            String getPath = "/averages/get-last/" + id + ":" + n;

            clientResponse = RestFunc.getRequest(client, serverAddress + getPath);

            System.out.println("The average of the last " + n + " measurements for robot " + id + " is: ");
            System.out.println(clientResponse.getEntity(String.class));

        }

        public static void requestAveragesBetweenTime(long t1, long t2) {

            String getPath = "/averages/get-between/" + t1 + ":" + t2;

            clientResponse = RestFunc.getRequest(client, serverAddress + getPath);

            System.out.println("The average of the measurements between " + t1 + " and " + t2 + " is: ");
            System.out.println(clientResponse.getEntity(String.class));

        }

        public static void getAllRobots() {

            System.out.println("The robots currently in Greenfield are: ");

            String getPath = "/robots";
            System.out.println("calling: " + getPath);

            clientResponse = RestFunc.getRequest(client, serverAddress + getPath);

            RobotList list = clientResponse.getEntity(RobotList.class);
            for (RobotInfo r : list.getRobotslist()) {
                System.out.println("ROBOT-" + r.getId() + " in district: " + r.getDistrict());
            }

        }

    }
