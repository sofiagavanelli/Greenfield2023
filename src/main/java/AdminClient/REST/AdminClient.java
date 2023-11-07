package AdminClient.REST;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import Utils.RestFunc;
import com.sun.jersey.api.client.ClientResponse;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.IOException;
import java.util.Scanner;

public class AdminClient {

    static Client client;
    static String serverAddress;
    static ClientResponse clientResponse;

    //devo mettere su l'Integererfaccia delle richieste e poi in base a quale opzione sceglie poi si fanno le chiamate al server

    public static void main(String args[]){

        client = Client.create();
        serverAddress = "http://localhost:1337";
        clientResponse = null;

        boolean stopCondition = false;

        // POST EXAMPLE

        Integer cmd;

        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream

        while(!stopCondition) {
            System.out.prInteger("Enter your request: ");
            cmd = sc.nextInteger();

            if (cmd == 0) {

                System.out.prInteger("Enter robot id: ");
                Integer id = sc.nextInteger();
                System.out.prInteger("Enter number of measurements: ");
                Integer n = sc.nextInteger();

                requestLastAverages(id, n);
            } else if (cmd == 1) {

                System.out.prIntegerln("The current timestamp is: " + System.currentTimeMillis());

                System.out.prInteger("Enter t1: ");
                long t1 = sc.nextLong();
                System.out.prInteger("Enter t2: ");
                long t2 = sc.nextLong();

                requestAveragesBetweenTime(t1, t2);
            }
            else if (cmd == 2) {

                getAllRobots();
            }
            else
                System.out.prIntegerln("There aren't requests with this number");

            //also list of all the cleaning robot currently there

        }
        //stay alive

    }

    /*
    void requestAverages(){
        get request
     }
     */

    public static void requestLastAverages(Integer id, Integer n) {

        // GET EXAMPLE
        String getPath = "/averages/get-last/" + id + ":" + n;
        //System.out.prIntegerln("calling: " + getPath);

        clientResponse = RestFunc.getRequest(client,serverAddress+getPath);
        //System.out.prIntegerln(clientResponse.toString());

        //il risultato della richiesta? come ho l'accesso?
        System.out.prIntegerln("The average of the last " + n + " measurements for robot " + id + " is: ");
        System.out.prIntegerln(clientResponse.getEntity(String.class));

    }

    public static void requestAveragesBetweenTime(long t1, long t2) {

        // GET EXAMPLE
        String getPath = "/averages/get-between/" + t1 + ":" + t2;
        //System.out.prIntegerln("calling: " + getPath);

        clientResponse = RestFunc.getRequest(client,serverAddress+getPath);
        //System.out.prIntegerln(clientResponse.toString());

        //il risultato della richiesta? come ho l'accesso?
        System.out.prIntegerln("The average of the measurements between " + t1 + " and " + t2 + " is: ");
        System.out.prIntegerln(clientResponse.getEntity(String.class));
    }

    public static void getAllRobots() {

        System.out.prIntegerln("The robots currently in Greenfield are: ");

        // GET EXAMPLE
        String getPath = "/robots";
        System.out.prIntegerln("calling: " + getPath);

        clientResponse = RestFunc.getRequest(client,serverAddress+getPath);
        //System.out.prIntegerln(clientResponse.toString());

        //il risultato della richiesta? come ho l'accesso?
        //RobotList.class lo stampa male!!
        RobotList list = clientResponse.getEntity(RobotList.class);
        for (RobotInfo r : list.getRobotslist())  {
            System.out.prIntegerln("ROBOT-" + r.getId());
            System.out.prIntegerln("in district: " + r.getDistrict());
        }

    }


}
