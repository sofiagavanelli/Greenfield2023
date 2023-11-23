package Utils;

import AdminServer.AdminServer;
import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import AdminServer.beans.RobotPositions;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.Response;

public class RestFunc {

    static ClientConfig config = new DefaultClientConfig();
    static Client client = Client.create(config);
    static String serverAddress = "http://localhost:1337";
    static ClientResponse clientResponse = null;

    private static final Logger logger = Logger.getLogger(RestFunc.class.getSimpleName());

    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %3$s : %5$s %n");
        Logger.getLogger("com.sun.jersey").setLevel(Level.SEVERE);
    }

    public static boolean addNewRobot(int botId, int botPort) {

        // POST EXAMPLE
        int botDistrict = 0, x = 0, y = 0;

        String postPath = "/robots/add";
        RobotInfo bot1 = new RobotInfo(botId, botPort);
        clientResponse = RestFunc.postRequest(client,serverAddress+postPath, bot1);
        //System.out.println(clientResponse.toString());

        assert clientResponse != null;
        if(clientResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println(clientResponse.toString());
            return false;
        }
        else {
            System.out.println(clientResponse.toString());
            //qui cosa sto facendo: chiedo al server la lista dei robot
            RobotList robs = clientResponse.getEntity(RobotList.class);
            List<RobotInfo> copyRobs = robs.getRobotslist();
            RobotList.getInstance().setRobotslist(copyRobs);

            //mettere 3 funzioni apposta?
            for (RobotInfo r : robs.getRobotslist()) {
                if (r.getId() == botId) {
                    botDistrict = r.getDistrict();
                    x = r.getX();
                    y = r.getY();
                } else
                    RobotPositions.getInstance().addIntoDistribution(r.getDistrict(), r.getId());
            }

            RobotInfo.getInstance().setAll(botId, botDistrict, x, y, botPort);

            return true;
        }

    }

    public static void deleteRobot(int id) {

        //delete request
        String deletePath = "/robots/remove";

        clientResponse = RestFunc.deleteRequest(client,serverAddress+deletePath, id);
        assert clientResponse != null;
        System.out.println(clientResponse.toString());

    }


    /*****utility*/

    public static ClientResponse postRequest(Client client, String url, RobotInfo r) {

        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(r);
        //System.out.println(input);

        try {
            return webResource.type("application/json").post(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            logger.severe("Server non disponibile");
            return null;
        }
    }

    public static ClientResponse getRequest(Client client, String url){
        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            logger.severe("Server non disponibile");
            return null;
        }
    }

    public static ClientResponse deleteRequest(Client client, String url, int id) {

        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(id);
        //System.out.println(input);

        try {
            return webResource.type("application/json").delete(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            logger.severe("Server non disponibile");
            return null;
        }
    }

}
