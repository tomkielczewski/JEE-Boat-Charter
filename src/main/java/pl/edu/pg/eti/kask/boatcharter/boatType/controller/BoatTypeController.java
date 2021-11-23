package pl.edu.pg.eti.kask.boatcharter.boatType.controller;

import pl.edu.pg.eti.kask.boatcharter.boatType.dto.CreateBoatTypeRequest;
import pl.edu.pg.eti.kask.boatcharter.boatType.dto.GetBoatTypeResponse;
import pl.edu.pg.eti.kask.boatcharter.boatType.dto.GetBoatTypesResponse;
import pl.edu.pg.eti.kask.boatcharter.boatType.dto.UpdateBoatTypeRequest;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.boatType.service.BoatTypeService;
import pl.edu.pg.eti.kask.boatcharter.user.entity.UserRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for {@link BoatType} entity.
 */
@Path("/boat_types")
@RolesAllowed(UserRoles.USER)
public class BoatTypeController {

    /**
     * Service for managing boatTypes.
     */
    private BoatTypeService service;

    /**
     * JAX-RS requires no-args constructor.
     */
    public BoatTypeController() {
    }

    /**
     * @param service service for managing boatTypes
     */
    @EJB
    public void setService(BoatTypeService service) {
        this.service = service;
    }

    /**
     * @return response with available boatTypes
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoatTypes() {
        return Response
                .ok(GetBoatTypesResponse.entityToDtoMapper().apply(service.findAll()))
                .build();
    }

    /**
     *
     * @param id id of the boatType
     * @return response with selected boatType or 404 status code
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoatType(@PathParam("id") Long id) {
        Optional<BoatType> boatType = service.find(id);
        if (boatType.isPresent()) {
            return Response
                    .ok(GetBoatTypeResponse.entityToDtoMapper().apply(boatType.get()))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response createBoatType(CreateBoatTypeRequest request) {

        List<BoatType> boatTypes = service.findAll();
        long newBoatTypeId;
        if (!boatTypes.isEmpty()){
            newBoatTypeId = Collections.max(boatTypes
                    .stream()
                    .map(BoatType::getId)
                    .collect(Collectors.toList())) + 1;
        }else {
            newBoatTypeId = 1L;
        }
        BoatType boatType = CreateBoatTypeRequest.dtoToEntityMapper().apply(request);
        boatType.setId(newBoatTypeId);
        service.create(boatType);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response updateBoatType(@PathParam("id") Long id, UpdateBoatTypeRequest request){

        Optional<BoatType> boatType = service.find(id);
        if (boatType.isPresent()) {
            UpdateBoatTypeRequest.dtoToEntityUpdater().apply(boatType.get(), request);
            service.update(boatType.get());
            return Response.status(Response.Status.ACCEPTED).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    @DELETE
//    public Response deleteBoatTypes(){
//        service.deleteAll();
//        return Response.status(Response.Status.OK).build();
//    }

    @DELETE
    @Path("{id}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response deleteBoatType(@PathParam("id") Long id){
        Optional<BoatType> boatType = service.find(id);
        if (boatType.isPresent()) {
            service.delete(id);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
}
