package Utils;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestFunc {

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

    public static ClientResponse deleteRequest(Client client, String url, int id) {
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
