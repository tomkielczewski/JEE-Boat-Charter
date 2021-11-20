package pl.edu.pg.eti.kask.boatcharter.boat.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.boat.model.BoatsModel;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;
import pl.edu.pg.eti.kask.boatcharter.boatType.service.BoatTypeService;

import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Optional;

/**
 * View bean for rendering list of boats.
 */
@ViewScoped
@Named
public class BoatList implements Serializable {

    @Setter
    @Getter
    private long boatTypeId;

    /**
     * Service for managing boats.
     */
    private final BoatService boatService;

    /**
     * Service for managing boatTypes.
     */
    private final BoatTypeService boatTypeService;

    /**
     * Boats list exposed to the view.
     */
    private BoatsModel boats;

    @Inject
    public BoatList(BoatService boatService, BoatTypeService boatTypeService) {
        this.boatService = boatService;
        this.boatTypeService = boatTypeService;
    }

    /**
     * In order to prevent calling boatService on different steps of JSF request lifecycle, model property is cached using
     * lazy getter.
     *
     * @return all boats
     */
    public BoatsModel getBoats() {
        if (boats == null) {
            Optional<BoatType> boatType = boatTypeService.find(this.boatTypeId);
            boatType.ifPresent(value ->
                    boats = BoatsModel.entityToModelMapper()
                            .apply(boatService.findAll(value)));
        }
        return boats;
    }

    /**
     * Action for clicking delete action.
     *
     * @param boat boat to be removed
     * @return navigation case to list_boats
     */
    public String deleteAction(BoatsModel.Boat boat) {
        boatService.delete(boat.getId());
        return "boat_type_view?id=" + this.boatTypeId + "&faces-redirect=true";
//        return "boat_type/boat_type_view?id=2&faces-redirect=true";

    }


}
