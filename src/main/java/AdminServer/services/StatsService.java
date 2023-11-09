package AdminServer.services;

import AdminServer.beans.PollutionStats;
import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("averages")
public class StatsService {

    //restituisce la lista
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAllAverages(){
        return Response.ok(PollutionStats.getInstance()).build();
    }

    @Path("get-last/{id}:{n}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getLast(@PathParam("id") int ID, @PathParam("n") int n) {

        Double average = PollutionStats.getInstance().getLast(ID, n);

        if(average != null)
            return Response.ok(average.toString()).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }

    @Path("get-between/{t1}:{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAllBetween(@PathParam("t1") long firstT, @PathParam("t2") long secondT) {

        Double average = PollutionStats.getInstance().getBetween(firstT, secondT);

        if(average != null)
            return Response.ok(average.toString()).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }


}
