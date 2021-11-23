package pl.edu.pg.eti.kask.boatcharter.boat.controller;


import pl.edu.pg.eti.kask.boatcharter.boat.dto.GetBoatResponse;
import pl.edu.pg.eti.kask.boatcharter.boat.dto.GetBoatsResponse;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;
import pl.edu.pg.eti.kask.boatcharter.user.entity.UserRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/boats")
@RolesAllowed(UserRoles.USER)
public class BoatController {

    BoatService boatService;

    @EJB
    public void setBoatService(BoatService boatService) {
        this.boatService = boatService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response getBoats() {
        List<Boat> boats = boatService.findAll();
        if (boats.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(GetBoatsResponse.entityToDtoMapper().apply(boats)).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response getBoat(@PathParam("id") Long id){
        Optional<Boat> boat = boatService.find(id);
        if(boat.isPresent()){
            return Response.ok(GetBoatResponse.entityToDtoMapper().apply(boat.get())).build();
        }else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
