package pl.edu.pg.eti.kask.boatcharter.boatType.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.boatType.model.BoatTypeModel;
import pl.edu.pg.eti.kask.boatcharter.boatType.service.BoatTypeService;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
public class BoatTypeView implements Serializable {

    private BoatTypeService service;

    @Setter
    @Getter
    private Long id;

    @Getter
    private BoatTypeModel boatType;

    public BoatTypeView() {

    }

    @EJB
    public void setBoatTypeService(BoatTypeService service) {
        this.service = service;
    }


    public void init() throws IOException{
        Optional<BoatType> boatType = service.find(id);
        if(boatType.isPresent()) {
            this.boatType = BoatTypeModel.entityToModelMapper().apply(boatType.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "BoatType not found");
        }
    }

}
