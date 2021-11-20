package pl.edu.pg.eti.kask.boatcharter.user.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;
import pl.edu.pg.eti.kask.boatcharter.user.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding user entity.
 */
@ApplicationScoped
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class UserService {

    private UserRepository repository;

    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
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

    @Transactional
    public void create(User user) {
        repository.create(user);
    }

    /**
     * @param user user to be removed
     */
//    @RolesAllowed(UserRoles.ADMIN)
    @Transactional
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
