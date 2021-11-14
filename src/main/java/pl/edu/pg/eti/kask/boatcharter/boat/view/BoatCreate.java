package pl.edu.pg.eti.kask.boatcharter.boat.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.model.BoatCreateModel;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;
import pl.edu.pg.eti.kask.boatcharter.boatType.service.BoatTypeService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class BoatCreate implements Serializable {

    private final BoatService boatService;

    private final BoatTypeService boatTypeService;

    @Getter
    private BoatCreateModel boat;

    @Getter
    @Setter
    private Long boatTypeId;

    @Getter
    private Long id;

    @Inject
    public BoatCreate(BoatService boatService, BoatTypeService boatTypeService) {
        this.boatService = boatService;
        this.boatTypeService = boatTypeService;
    }

    public void init(){
        List<Boat> boats = boatService.findAll();
        if (!boats.isEmpty()){
            this.id = Collections.max(boats
                    .stream()
                    .map(Boat::getId)
                    .collect(Collectors.toList()));
        }else {
            this.id = 1L;
        }

        this.boat = BoatCreateModel.builder().build();
        this.boat.setBoatType(boatTypeService.find(this.boatTypeId).get());
        this.boat.setId(id);
    }

    public String saveAction() {
        boatService.create(BoatCreateModel.entityToModelCreator().apply(boat));
        return "/boat_type/boat_type_view?id=" + this.boatTypeId + "&faces-redirect=true";
//        return "/boat_type/boat_type_view?id=" + 1 + "&faces-redirect=true";
    }
}
