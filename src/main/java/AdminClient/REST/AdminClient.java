package AdminClient.REST;

import AdminServer.beans.RobotInfo;
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

    //devo mettere su l'interfaccia delle richieste e poi in base a quale opzione sceglie poi si fanno le chiamate al server

    public static void main(String args[]){

        client = Client.create();
        serverAddress = "http://localhost:1337";
        clientResponse = null;

        // POST EXAMPLE

        int cmd;

        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        System.out.print("Enter your request: ");
        cmd = sc.nextInt();

        if(cmd == 0) {

            System.out.print("Enter robot id: ");
            int id = sc.nextInt();
            System.out.print("Enter number of measurements: ");
            int n = sc.nextInt();

            requestAllAverages(id, n);
        }


        //stay alive

    }

    /*
    void requestAverages(){
        get request
     }
     */

    public static void requestAllAverages(int id, int n) {

        // GET EXAMPLE
        String postPath = "/averages/get-last/" + id + ":" + n;

        clientResponse = RestFunc.getRequest(client,serverAddress+postPath);
        //System.out.println(clientResponse.toString());

        //il risultato della richiesta? come ho l'accesso?
        System.out.println("The average of the last " + n + " measurements for robot " + id + " is: ");
        System.out.println(clientResponse.getEntity(String.class));

    }


}
