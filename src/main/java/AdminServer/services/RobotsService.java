package AdminServer.services;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import AdminServer.beans.RobotPositions;

import java.util.*;
import java.util.logging.Logger;

@Path("robots")
public class RobotsService {

    private static final Logger logger = Logger.getLogger(RobotsService.class.getSimpleName());

    static {
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %3$s : %5$s %n");
    }

    //restituisce la lista di utenti
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getRobotsList(){
        return Response.ok(RobotList.getInstance()).build();
    }

    //permette di inserire un robot: ID, IP address (i.e., localhost), The port number on communications
    //with the other robots
    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"}) //use of json?
    public Response addRobot(RobotInfo r){

        if(!RobotPositions.getInstance().newPosition(r))
            return Response.status(Response.Status.BAD_REQUEST).entity("Something went wrong calculating the position").build();

        if(RobotList.getInstance().add(r)) {
            logger.info("There is a new robot in Greenfield: ROBOT-" + r.getId() + " in district " + r.getDistrict());
            return Response.ok(RobotList.getInstance()).build();
        }
        else
            return Response.status(Response.Status.BAD_REQUEST).entity("ID or port already in use").build();

    }

    @Path("remove")
    @DELETE
    @Consumes({"application/json", "application/xml"}) //use of json?
    public Response removeRobot(int ID){

        if(RobotList.getInstance().remove(ID)) {
            logger.warning("ROBOT-" + ID + " left Greenfield");
            return Response.ok(RobotList.getInstance()).build();
        }

        else return Response.status(Response.Status.BAD_REQUEST).entity("Problems deleting robot").build();
    }

    //permette di prelevare un utente con un determinato nome
    @Path("get/districts")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getDistribution(){

        if(RobotPositions.getInstance().getRobotsDistricts() != null)
            return Response.ok(RobotPositions.getInstance().getRobotsDistricts()).build();
        else
            return Response.status(Response.Status.NOT_FOUND).entity("Problems obtaining list").build();
    }

}
