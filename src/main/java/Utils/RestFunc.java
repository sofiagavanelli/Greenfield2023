package Utils;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.util.List;

import AdminServer.beans.RobotPositions;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;

public class RestFunc {

    static ClientConfig config = new DefaultClientConfig();
    static Client client = Client.create(config);
    static String serverAddress = "http://localhost:1337";
    static ClientResponse clientResponse = null;

    public static void addNewRobot(int botId, int botPort) {

        // POST EXAMPLE
        int botDistrict = 0, x = 0, y = 0;

        String postPath = "/robots/add";
        RobotInfo bot1 = new RobotInfo(botId, botPort);
        clientResponse = RestFunc.postRequest(client,serverAddress+postPath, bot1);
        System.out.println(clientResponse.toString());

        //qui cosa sto facendo: chiedo al server la lista dei robot
        RobotList robs = clientResponse.getEntity(RobotList.class);
        List<RobotInfo> copyRobs = robs.getRobotslist();
        RobotList.getInstance().setRobotslist(copyRobs);

        //mettere 3 funzioni apposta?
        for (RobotInfo r : robs.getRobotslist()){
            if(r.getId() == botId) {
                botDistrict = r.getDistrict();
                x = r.getX();
                y = r.getY();
            }
            else
                RobotPositions.getInstance().addIntoDistribution(r.getDistrict(), r.getId());
        }

        RobotInfo.getInstance().setAll(botId, botDistrict, x, y, botPort);

        System.out.println("the current position is in the district: " + botDistrict + " x: " + x + " y: " + y);


    }


    public static String getDistricts() {

        // GET EXAMPLE
        String getPath = "/robots/get/districts";

        clientResponse = RestFunc.getRequest(client,serverAddress+getPath);
        //System.out.println(clientResponse.toString());

        //il risultato della richiesta? come ho l'accesso?
        System.out.println("The distribution is: ");
        System.out.println(clientResponse.getEntity(String.class));

        return(clientResponse.getEntity(String.class));
    }

    public static void deleteRobot(int id) {

        //delete request
        String deletePath = "/robots/remove";

        clientResponse = RestFunc.deleteRequest(client,serverAddress+deletePath, id);
        System.out.println(clientResponse.toString());

    }


    /*****utility*/

    public static ClientResponse postRequest(Client client, String url, RobotInfo r) {

        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(r);
        System.out.println(input);

        try {
            return webResource.type("application/json").post(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

    public static ClientResponse getRequest(Client client, String url){
        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

    public static ClientResponse deleteRequest(Client client, String url, int id) {

        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(id);
        System.out.println(input);

        try {
            return webResource.type("application/json").delete(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

}
