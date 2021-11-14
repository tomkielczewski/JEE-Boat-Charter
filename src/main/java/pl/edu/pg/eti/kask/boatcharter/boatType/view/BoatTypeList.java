package pl.edu.pg.eti.kask.boatcharter.boatType.view;

import pl.edu.pg.eti.kask.boatcharter.boatType.model.BoatTypesModel;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;
import pl.edu.pg.eti.kask.boatcharter.boatType.service.BoatTypeService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * View bean for rendering list of boatTypes.
 */
@RequestScoped
@Named
public class BoatTypeList implements Serializable {

    /**
     * Service for managing boatTypes.
     */
    private final BoatTypeService service;

    private final BoatService boatService;

    /**
     * BoatTypes list exposed to the view.
     */
    private BoatTypesModel boatTypes;

    @Inject
    public BoatTypeList(BoatTypeService service, BoatService boatService) {
        this.service = service;
        this.boatService = boatService;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached using
     * lazy getter.
     *
     * @return all boatTypes
     */
    public BoatTypesModel getBoatTypes() {
        if (boatTypes == null) {
            boatTypes = BoatTypesModel.entityToModelMapper().apply(service.findAll());
        }
        return boatTypes;
    }

    /**
     * Action for clicking delete action.
     *
     * @param boatType boatType to be removed
     * @return navigation case to list_boatTypes
     */
    public String deleteAction(BoatTypesModel.BoatType boatType) {
        service.delete(boatType.getId());
        return "boat_type_list?faces-redirect=true";
    }

}
