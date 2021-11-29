package pl.edu.pg.eti.kask.boatcharter.boat.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.model.BoatEditModel;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;
import pl.edu.pg.eti.kask.boatcharter.user.service.UserService;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@ViewScoped
@Named
public class BoatEdit implements Serializable {

    private BoatService service;

    private UserService userService;


    @Setter
    @Getter
    private Long id;

    @Getter
    private BoatEditModel boat;

    public BoatEdit() {

    }

    @EJB
    public void setBoatService(BoatService boatService) {
        this.service = boatService;
    }

    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
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


//    public String saveAction() {
//        service.update(BoatEditModel.modelToEntityUpdater().apply(service.find(id).orElseThrow(), boat));
//        return "boat_view?id=" + this.id + "&faces-redirect=true";
//    }

    public String saveAction() throws IOException {
        try {
            service.update(BoatEditModel.
                    modelToEntityUpdater()
                    .apply(service.find(id).orElseThrow(), boat));

            return "boat_view?id=" + this.id + "&faces-redirect=true";
//            return "boat_view?id=" + this.id;
//            return "boat_type/boat_type_view?id=" + this.id;
        } catch (EJBException ex) {
            if (ex.getCause() instanceof OptimisticLockException) {
                init();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Version collision"));
            }
            return null;
        }
    }

//    public String saveAction() throws IOException {
//        try {
//            service.update(BoatEditModel
//                    .modelToEntityUpdater(user -> userService.find(user.getLogin()).orElse(null))
//                    .apply(service.find(id).orElseThrow(), boat));
//            return "boat_view?id=" + this.id;
//        } catch (EJBException ex) {
//            if (ex.getCause() instanceof OptimisticLockException) {
//                init();
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Version collision"));
//            }
//            return null;
//        }
//    }


    }
