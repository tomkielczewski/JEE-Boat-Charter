package pl.edu.pg.eti.kask.boatcharter.boat.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.model.BoatEditModel;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@ViewScoped
@Named
public class BoatEdit implements Serializable {

    private final BoatService service;


    @Setter
    @Getter
    private Long id;

    @Getter
    private BoatEditModel boat;


    @Inject
    public BoatEdit(BoatService service) {
        this.service = service;
    }



    public void init() throws IOException {
        Optional<Boat> boat = service.find(id);
        if (boat.isPresent()) {
            this.boat = BoatEditModel.entityToModelMapper().apply(boat.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Boat not found");
        }
    }


    public String saveAction() {
        service.update(BoatEditModel.modelToEntityUpdater().apply(service.find(id).orElseThrow(), boat));
        return "boat_view?id=" + this.id + "&faces-redirect=true";
    }

}
