package pl.edu.pg.eti.kask.boatcharter.user.repository;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.boatcharter.repository.Repository;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;

@Dependent
@Log
public class UserRepository implements Repository<User, String> {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }


    @Override
    public Optional<User> find(String login) {
        log.info(String.format("EntityManager for %s %s find", this.getClass(), em));
        return Optional.ofNullable(em.find(User.class, login));
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public void create(User entity) {
        log.info(String.format("EntityManager for %s %s create", this.getClass(), em));
        em.persist(entity);
    }

    @Override
    public void delete(User entity) {
        em.remove(em.find(User.class, entity.getLogin()));
    }

    @Override
    public void update(User entity) {
        em.merge(entity);
    }

    @Override
    public void detach(User entity) {
        em.detach(entity);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        try {
            return Optional.of(em.createQuery("select u from User u where u.login = :login and u.password = :password", User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
