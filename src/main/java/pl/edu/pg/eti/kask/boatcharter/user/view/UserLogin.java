package pl.edu.pg.eti.kask.boatcharter.user.view;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

/**
 * View bean for handling form login.
 */
@RequestScoped
@Named
@Log
public class UserLogin {

    private HttpServletRequest request;

    private SecurityContext securityContext;

    private FacesContext facesContext;

    @Inject
    public UserLogin(HttpServletRequest request, SecurityContext securityContext, FacesContext facesContext) {
        this.request = request;
        this.securityContext = securityContext;
        this.facesContext = facesContext;
    }

    /**
     * View model, username.
     */
    @Getter
    @Setter
    private String login;

    /**
     * VIew model, password.
     */
    @Getter
    @Setter
    private String password;

    /**
     * Action initiated by clicking login button.
     */
    @SneakyThrows
    public void loginAction() {
        Credential credential = new UsernamePasswordCredential(login, new Password(password));
        AuthenticationStatus status = securityContext.authenticate(request,  getHttpResponseFromFacesContext(),
                withParams().credential(credential));
        facesContext.responseComplete();
    }

    private HttpServletResponse getHttpResponseFromFacesContext() {
        return (HttpServletResponse) facesContext.getExternalContext().getResponse();
    }
}
