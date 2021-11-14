package pl.edu.pg.eti.kask.boatcharter.user.authentication.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.boatcharter.digest.Sha256Utility;
import pl.edu.pg.eti.kask.boatcharter.user.context.UserContext;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;
import pl.edu.pg.eti.kask.boatcharter.user.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * At this point we are not using any complicated Jakarta EE authentication mechanism. This calls allows to separate
 * authentication from filters and CDI beans.
 */
@ApplicationScoped
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class AuthenticationService {

    /**
     * Service for managing users.
     */
    private UserService service;

    /**
     * Session user context. Injected using proxy so always points for session from particular request.
     */
    private UserContext context;

    @Inject
    public AuthenticationService(UserService service, UserContext context) {
        this.service = service;
        this.context = context;
    }

    /**
     * Checks if login and password are correct and if yes then stores user in session context.
     *
     * @param login    username
     * @param password user's password
     * @return true if user was successfully authenticated
     */
    public boolean authenticate(String login, String password) {
        Optional<User> user = service.find(login, Sha256Utility.hash(password));
        if (user.isPresent()) {
            //Put username in session where it will be present while session is valid.
            context.setPrincipal(user.get().getLogin());
            return true;
        } else {
            return false;
        }
    }

}
