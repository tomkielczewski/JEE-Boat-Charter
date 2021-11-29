package pl.edu.pg.eti.kask.boatcharter.user.repository;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.boatcharter.repository.Repository;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User_;

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
//        return em.createQuery("select u from User u", User.class).getResultList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        return em.createQuery(query).getResultList();
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
//        try {
//            return Optional.of(em.createQuery("select u from User u where u.login = :login and u.password = :password", User.class)
//                    .setParameter("login", login)
//                    .setParameter("password", password)
//                    .getSingleResult());
//        } catch (NoResultException ex) {
//            return Optional.empty();
//        }
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root)
                    .where(cb.and(
//                            cb.equal(root.get(User_.user).get(User_.name), user.getName()),
                            cb.equal(root.get(User_.login), login),
                            cb.equal(root.get(User_.password), password)));
            return Optional.of(em.createQuery(query).getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
