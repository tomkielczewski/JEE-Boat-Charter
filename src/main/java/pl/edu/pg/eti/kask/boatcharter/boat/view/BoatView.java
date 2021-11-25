package pl.edu.pg.eti.kask.boatcharter.boat.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.model.BoatModel;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@RequestScoped
@Named
public class BoatView implements Serializable {

    /**
     * Service for managing boats.
     */
    private BoatService service;

    /**
     * Boat id.
     */
    @Setter
    @Getter
    private Long id;

    /**
     * Boat exposed to the view.
     */
    @Getter
    private BoatModel boat;

    public BoatView() {
    }

    @EJB
    public void setService(BoatService service) {
        this.service = service;
    }


    public void init() throws IOException {
        Optional<Boat> boat = service.find(id);
        if (boat.isPresent()) {
            this.boat = BoatModel.entityToModelMapper().apply(boat.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Boat not found");
        }
    }
}
