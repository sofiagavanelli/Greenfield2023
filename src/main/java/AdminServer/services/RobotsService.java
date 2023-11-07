package AdminServer.services;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import AdminServer.beans.RobotPositions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Path("robots")
public class RobotsService {

    private List<Integer> botDistricts = new ArrayList<Integer>();

    private Integer x = 0;
    private Integer y = 0;
    private Integer district;

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

        if(newPosition())
            r.setCoordinates(this.x, this.y, this.district);
        else
            return Response.status(Response.Status.BAD_REQUEST).entity("Something went wrong calculating the position").build();

        if(RobotList.getInstance().add(r))
            return Response.ok(RobotList.getInstance()).build();

        else return Response.status(Response.Status.BAD_REQUEST).entity("ID already in use").build();
    }

    @Path("remove")
    @DELETE
    @Consumes({"application/json", "application/xml"}) //use of json?
    public Response removeRobot(Integer ID){

        if(RobotList.getInstance().remove(ID))
            return Response.ok(RobotList.getInstance()).build();

        else return Response.status(Response.Status.BAD_REQUEST).entity("problems eliminating robot").build();
    }

    //permette di prelevare un utente con un determinato nome
    /*@Path("get/{name}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getByName(@PathParam("name") String name){
        User u = Users.getInstance().getByName(name);
        if(u!=null)
            return Response.ok(u).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }*/

    private boolean newPosition() {

        Random pos = new Random();

        botDistricts = RobotPositions.getInstance().getDistricts();

        Integer d = 0;

        //sarebbe da mettere in modalità random!!
        for(Integer i=0; i<4; i++) {
            if(i==0 && botDistricts.get(i) == 0) {
                d = i + 1;
                System.out.println("DISTRETTO " + d + " bot dentro: " + botDistricts.get(i));
            }
            else {
                if (botDistricts.get(i) < botDistricts.get(d)) {
                    d = i + 1;
                    System.out.println("DISTRETTO " + d + " bot dentro: " + botDistricts.get(i));
                }
            }
        }

        //the real district [1,4] is the one chosen [0,4] plus 1
        this.district =  d; //pos.nextInteger(4) + 1;
        //we put the elements in 0=1, 1=2, 2=3, 3=4 where (position=district)
        //botDistricts.add(district - 1, 1); //inutile?
        RobotPositions.getInstance().addDistricts(d - 1);

        this.x = pos.nextInt(5);
        this.y = pos.nextInt(5);

        if(this.district == 2) {
            this.x = this.x + 5;
        }
        else if(this.district == 3) {
            this.x = this.x + 5;
            this.y = this.y + 5;
        }
        else if(this.district == 4) {
            this.y = this.y + 5;
        }

        System.out.println("x: " + x);
        System.out.println("y: " + y);

        return true;

    }

}
