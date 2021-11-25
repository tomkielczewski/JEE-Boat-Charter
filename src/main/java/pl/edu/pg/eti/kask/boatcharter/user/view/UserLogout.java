package pl.edu.pg.eti.kask.boatcharter.user.view;

import lombok.SneakyThrows;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * View bean for handling form logout.
 */
@RequestScoped
@Named
public class UserLogout {

    /**
     * Current HTTP request.
     */
    private HttpServletRequest request;

    @Inject
    public UserLogout(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Action initiated by clicking logout button.
     *
     * @return navigation case to the same page
     */
    @SneakyThrows
    public String logoutAction() {
        request.logout();//Session invalidate can possibly not work with JASPIC.
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }

}
