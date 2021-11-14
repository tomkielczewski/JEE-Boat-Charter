package pl.edu.pg.eti.kask.boatcharter.user.context;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Information of logged user in session context. Used in view but not directly associated with them.
 */
@SessionScoped
@Named
public class UserContext implements Serializable {

    /**
     * Caller principal, logged user.
     */
    @Getter
    @Setter
    private String principal;

    /**
     * Authorization covers if someone (user) is authorized to do or view something. In this example it is enough to be
     * logged in order to be authorized to view stuff.
     *
     * @return true if principal is present in session context
     */
    public boolean isAuthorized() {
        return principal != null;
    }

}
