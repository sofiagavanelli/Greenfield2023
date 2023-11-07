package Utils;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.util.List;

public class RestFunc {

    static ClientConfig config = new DefaultClientConfig();
    static Client client = Client.create(config);
    static String serverAddress = "http://localhost:1337";
    static ClientResponse clientResponse = null;

    public static void addNewRobot(Integer botId, Integer botPort) {

        // POST EXAMPLE
        Integer botDistrict = 0, x = 0, y = 0;

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
        }

        RobotInfo.getInstance().setAll(botId, botDistrict, x, y, botPort);

    }


    public static void requestDistricts() {

    }

    public static void deleteRobot(Integer id) {

        //delete request
        String deletePath = "/robots/remove";

        clientResponse = RestFunc.deleteRequest(client,serverAddress+deletePath, id);
        System.out.println(clientResponse.toString());

    }






    /*****utility*/

    public static ClientResponse postRequest(Client client, String url, RobotInfo r) {
        //WebResource webResource = client.resource(url);
        //String input = new Gson().toJson(r);
        //return webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class);

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

    public static ClientResponse deleteRequest(Client client, String url, Integer id) {
        //WebResource webResource = client.resource(url);
        //String input = new Gson().toJson(r);
        //return webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class);

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
