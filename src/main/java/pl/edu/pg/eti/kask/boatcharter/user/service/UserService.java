package pl.edu.pg.eti.kask.boatcharter.user.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;
import pl.edu.pg.eti.kask.boatcharter.user.entity.UserRoles;
import pl.edu.pg.eti.kask.boatcharter.user.repository.UserRepository;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding user entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
@RolesAllowed(UserRoles.USER)
public class UserService {

    private UserRepository repository;

    /**
     * Build in security context.
     */
    private SecurityContext securityContext;

    /**
     * Password hashing algorithm.
     */
    private Pbkdf2PasswordHash pbkdf;

    @Inject
    public UserService(UserRepository repository, SecurityContext securityContext, Pbkdf2PasswordHash pbkdf) {
        this.repository = repository;
        this.securityContext = securityContext;
        this.pbkdf = pbkdf;
    }

    public Optional<User> find(String login) {
        return repository.find(login);
    }

    public Optional<User> find(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * @return logged user entity
     */
    public Optional<User> findCallerPrincipal() {
        if (securityContext.getCallerPrincipal() != null) {
            return find(securityContext.getCallerPrincipal().getName());
        } else {
            return Optional.empty();
        }
    }

    @PermitAll
    public void create(User user) {
        if (!securityContext.isCallerInRole(UserRoles.ADMIN)) {
            user.setRoles(List.of(UserRoles.USER));
        }
        user.setPassword(pbkdf.generate(user.getPassword().toCharArray()));
        repository.create(user);
    }

    /**
     * @param user user to be removed
     */
    @RolesAllowed(UserRoles.ADMIN)
    public void delete(User user) {
        repository.delete(user);
    }


    public void setAvatar(String userId, InputStream is) {
        repository.find(userId).ifPresent(user -> {
            try {
                user.setAvatar(is.readAllBytes());
                repository.update(user);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

    public void updateAvatar(String userId, InputStream is) {
        repository.find(userId).ifPresent(user -> {
            try {
                user.setAvatar(is.readAllBytes());
                repository.update(user);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

    public void deleteAvatar(String userId) {
        repository.find(userId).ifPresent(user -> {

            user.setAvatar(null);
            repository.update(user);

        });
    }

}
