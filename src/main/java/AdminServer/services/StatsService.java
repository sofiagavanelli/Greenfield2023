package AdminServer.services;

import AdminServer.beans.PollutionStats;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

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

        if(average != null && average > 0)
            return Response.ok(average.toString()).build();
        else
            return Response.status(Response.Status.NOT_FOUND).entity("Problems calculating your average, control your parameters").build();

    }

    @Path("get-between/{t1}:{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAllBetween(@PathParam("t1") long firstT, @PathParam("t2") long secondT) {

        Double average = PollutionStats.getInstance().getBetween(firstT, secondT);

        if(average != null && average > 0)
            return Response.ok(average.toString()).build();
        else
            return Response.status(Response.Status.NOT_FOUND).entity("Problems calculating your average, control your parameters").build();

    }


}
