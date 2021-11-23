package pl.edu.pg.eti.kask.boatcharter.boat.controller;



import pl.edu.pg.eti.kask.boatcharter.boat.dto.CreateBoatRequest;
import pl.edu.pg.eti.kask.boatcharter.boat.dto.GetBoatResponse;
import pl.edu.pg.eti.kask.boatcharter.boat.dto.GetBoatsResponse;
import pl.edu.pg.eti.kask.boatcharter.boat.dto.UpdateBoatRequest;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.boatType.service.BoatTypeService;
import pl.edu.pg.eti.kask.boatcharter.user.entity.UserRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("")
@RolesAllowed(UserRoles.USER)
public class UserBoatTypeBoatController {

    BoatTypeService boatTypeService;

    BoatService boatService;

    public UserBoatTypeBoatController() {
    }

    @EJB
    public void setBoatTypeService(BoatTypeService boatTypeService) {
        this.boatTypeService = boatTypeService;
    }

    @EJB
    public void setBoatService(BoatService boatService) {
        this.boatService = boatService;
    }

    /**
     * @param login user's username
     * @return response with available boats owned by selected user
     */
    @GET
    @Path("/users/{login}/boat_types/{boatTypeId}/boats")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response getBoats(@PathParam("login") String login, @PathParam("boatTypeId") Long boatTypeId) {
        Optional<BoatType> boatType = boatTypeService.find(boatTypeId);
        Optional<List<Boat>> boats = boatService.findAllByUserAndBoatType(login, boatType.get());
        if (boats.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        } else {
            return Response
                    .ok(GetBoatsResponse.entityToDtoMapper().apply(boats.get()))
                    .build();
        }
    }


    @GET
    @Path("user/boat_types/{boatTypeId}/boats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoats(@PathParam("boatTypeId") Long boatTypeId) {
        Optional<BoatType> boatType = boatTypeService.find(boatTypeId);
        if (boatType.isPresent()) {
            List<Boat> boats = boatService.findAllByBoatTypeForCallerPrincipal(boatType.get());
            if (boats.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok(GetBoatsResponse.entityToDtoMapper().apply(boats)).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("user/boat_types/{boatTypeId}/boats/{boatId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoat(@PathParam("boatTypeId") Long boatTypeId, @PathParam("boatId") Long boatId) {
        Optional<BoatType> boatType = boatTypeService.find(boatTypeId);
        Optional<Boat> boat = boatService.find(boatId);
        if(boatType.isPresent() && boat.isPresent()){
            if(boat.get().getBoatType().equals(boatType.get())){
                return Response.ok(GetBoatResponse.entityToDtoMapper()
                        .apply(boat.get()))
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Path("user/boat_types/{boatTypeId}/boats")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBoat(@PathParam("boatTypeId") Long boatTypeId, CreateBoatRequest request){
        Optional<BoatType> boatType = boatTypeService.find(boatTypeId);
        if(boatType.isPresent()){

            List<Boat> boats = boatService.findAll();
            long newBoatId;
            if (!boats.isEmpty()){
                newBoatId = Collections.max(boats
                        .stream()
                        .map(Boat::getId)
                        .collect(Collectors.toList())) + 1;
            }else {
                newBoatId = 1L;
            }

            Boat boat = CreateBoatRequest.dtoToEntityMapper(() -> null).apply(request);
            boat.setId(newBoatId);
            boat.setBoatType(boatType.get());
            boatService.createForCallerPrincipal(boat);
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @PUT
    @Path("users/{login}/boat_types/{boatTypeId}/boats/{boatId}")
    @RolesAllowed(UserRoles.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBoat(@PathParam("login") String login, @PathParam("boatTypeId") Long boatTypeId, @PathParam("boatId") Long boatId, UpdateBoatRequest request){

        Optional<BoatType> boatType = boatTypeService.find(boatTypeId);
        Optional<Boat> boat = boatService.find(boatId);
        if(boatType.isPresent() && boat.isPresent()){

            if(boat.get().getBoatType().equals(boatType.get())){

                UpdateBoatRequest.dtoToEntityUpdater().apply(boat.get(), request);

                try {
                    boatService.update(boat.get());
                } catch (EJBAccessException ex) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                }

                return Response.status(Response.Status.ACCEPTED).build();

            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        }else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("user/boat_types/{boatTypeId}/boats/{boatId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBoat(@PathParam("boatTypeId") Long boatTypeId, @PathParam("boatId") Long boatId, UpdateBoatRequest request){

        Optional<BoatType> boatType = boatTypeService.find(boatTypeId);
        Optional<Boat> boat = boatService.find(boatId);
        if(boatType.isPresent() && boat.isPresent()){

            if(boat.get().getBoatType().equals(boatType.get())){

                UpdateBoatRequest.dtoToEntityUpdater().apply(boat.get(), request);

                try {
                    boatService.update(boat.get());
                } catch (EJBAccessException ex) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                }

                return Response.status(Response.Status.ACCEPTED).build();

            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        }else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    @DELETE
//    @Path("user/boat_types/{boatTypeId}/boats")
//    public Response deleteBoats(@PathParam("boatTypeId") Long boatTypeId){
//        Optional<BoatType> boatType = boatTypeService.find(boatTypeId);
//        if(boatType.isPresent()){
//            boatService.delete(boatType.get());
//            return Response.status(Response.Status.OK).build();
//        }else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }

    @DELETE
    @RolesAllowed(UserRoles.ADMIN)
    @Path("users/{login}/boat_types/{boatTypeId}/boats/{boatId}")
    public Response deleteBoat(@PathParam("login") String login, @PathParam("boatTypeId") Long boatTypeId, @PathParam("boatId") Long boatId){
        Optional<BoatType> boatType = boatTypeService.find(boatTypeId);
        Optional<Boat> boat = boatService.find(boatId);
        if(boatType.isPresent() && boat.isPresent()) {

            if(boat.get().getBoatType().equals(boatType.get())){

                boatService.delete(boatId);
                return Response.status(Response.Status.OK).build();

            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }

        }else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("user/boat_types/{boatTypeId}/boats/{boatId}")
    public Response deleteBoat(@PathParam("boatTypeId") Long boatTypeId, @PathParam("boatId") Long boatId){
        Optional<BoatType> boatType = boatTypeService.find(boatTypeId);
        Optional<Boat> boat = boatService.findForCallerPrincipal(boatId);
        if(boatType.isPresent() && boat.isPresent()) {

            if(boat.get().getBoatType().equals(boatType.get())){

                boatService.delete(boatId);
                return Response.status(Response.Status.OK).build();

            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }

        }else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
