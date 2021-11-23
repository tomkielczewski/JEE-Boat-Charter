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
import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for {@link Boat} entity. The controller uses hierarchical mapping relative to users.
 */
@Path("")
@RolesAllowed(UserRoles.USER)
public class UserBoatController {

    /**
     * Service for managing boats.
     */
    private BoatService boatService;

    /**
     * Service for managing boatTypes.
     */
    private BoatTypeService boatTypeService;

    /**
     * JAX-RS requires no-args constructor.
     */
    public UserBoatController() {
    }

    /**
     * @param boatService service for managing boats
     */
    @EJB
    public void setBoatService(BoatService boatService) {
        this.boatService = boatService;
    }

    /**
     * @param boatTypeService service for managing boatTypes
     */
    @EJB
    public void setBoatTypeService(BoatTypeService boatTypeService) {
        this.boatTypeService = boatTypeService;
    }

    /**
     * @param login user's username
     * @return response with available boats owned by selected user
     */
    @GET
    @Path("/users/{login}/boats")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response getBoats(@PathParam("login") String login) {
        Optional<List<Boat>> boats = boatService.findAllByUser(login);
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

    /**
     * @param login user's username
     * @param id    id of the boat
     * @return response with selected boat or 404 status code
     */
    @GET
    @Path("/users/{login}/boats/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response getBoat(@PathParam("login") String login, @PathParam("id") Long id) {
        Optional<Boat> boat = boatService.findByUser(login, id);
        if (boat.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        } else {
            return Response
                    .ok(GetBoatResponse.entityToDtoMapper().apply(boat.get()))
                    .build();
        }
    }


    /**
     * @return response with available boats owned by logged user
     */
    @GET
    @Path("/user/boats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserBoats() {
        return Response
                .ok(GetBoatsResponse.entityToDtoMapper().apply(boatService.findAllForCallerPrincipal()))
                .build();

    }

    /**
     * @param id id of the boat
     * @return response with selected boat or 404 status code
     */
    @GET
    @Path("/user/boats/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserBoat(@PathParam("id") Long id) {
        Optional<Boat> boat = boatService.findForCallerPrincipal(id);
        if (boat.isPresent()) {
            return Response
                    .ok(GetBoatResponse.entityToDtoMapper().apply(boat.get()))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

//    /**
//     * Creates new boat.
//     *
//     * @param request parsed request body containing info about new boat
//     * @return response with created code and new boat location url
//     */
//    @POST
//    @Path("/user/boats")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response postUserBoat(CreateBoatRequest request) {
//        Boat boat = CreateBoatRequest
//                .dtoToEntityMapper(name -> boatTypeService.find(name).orElse(null), () -> null)
//                .apply(request);
//        boatService.createForCallerPrincipal(boat);
//        return Response
//                .created(UriBuilder
//                        .fromMethod(UserBoatController.class, "getUserBoat")
//                        .build(boat.getId()))
//                .build();
//    }



//    /**
//     * Updates boat.
//     *
//     * @param request parsed request body containing info about boat
//     * @return response with accepted code
//     */
//    @PUT
//    @Path("/users/{login}/boats/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response putBoat(@PathParam("login") String login, @PathParam("id") Long id, UpdateBoatRequest request) {
//        Optional<Boat> boat = boatService.findByUser(login, id);
//
//        if (boat.isPresent()) {
//            UpdateBoatRequest.dtoToEntityUpdater().apply(boat.get(), request);
//
//            try {
//                boatService.update(boat.get());
//            } catch (EJBAccessException ex) {
//                return Response.status(Response.Status.FORBIDDEN).build();
//            }
//            return Response.noContent().build();
//        } else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }
//


}
