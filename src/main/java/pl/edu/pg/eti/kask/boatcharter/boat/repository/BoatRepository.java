package pl.edu.pg.eti.kask.boatcharter.boat.repository;

import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat_;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.repository.Repository;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Repository for boat entity. Repositories should be used in business layer (e.g.: in services).
 */
@Dependent
public class BoatRepository implements Repository<Boat, Long> {

    /**
     * Connection with the database (not thread safe).
     */
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Boat> find(Long id) {
        return Optional.ofNullable(em.find(Boat.class, id));
    }

    @Override
    public List<Boat> findAll() {
//        return em.createQuery("select e from Boat e", Boat.class).getResultList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boat> query = cb.createQuery(Boat.class);
        Root<Boat> root = query.from(Boat.class);
        query.select(root);
        return em.createQuery(query).getResultList();

    }

    @Override
    public void create(Boat entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Boat entity) {
        em.remove(em.find(Boat.class, entity.getId()));
    }


    @Override
    public void update(Boat entity) {
        em.merge(entity);
    }

    @Override
    public void detach(Boat entity) {
        em.detach(entity);
    }
    

    public void deleteByBoatType(BoatType boatType) {
        List<Boat> boats = findAllByType(boatType);
        boats.forEach(this::delete);
    }

    public void deleteAll() {
//        em.createQuery("select e from Boat e", Boat.class)
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boat> query = cb.createQuery(Boat.class);
        Root<Boat> root = query.from(Boat.class);
        query.select(root);
        em.createQuery(query)
                .getResultList().forEach(this::delete);
    }

    /**
     * Seeks for all boats of boatType.
     *
     * @param boatType boats' BoatType
     * @return list (can be empty) of boats
     */
    public List<Boat> findAllByType(BoatType boatType) {
//        return em.createQuery("select e from Boat e where e.boatType = :boatType", Boat.class)
//                .setParameter("boatType", boatType)
//                .getResultList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boat> query = cb.createQuery(Boat.class);
        Root<Boat> root = query.from(Boat.class);
        query.select(root)
                .where(cb.equal(root.get(Boat_.boatType), boatType));
        return em.createQuery(query).getResultList();
    }


    /**
     * Seeks for single user's boat.
     *
     * @param id   boat's id
     * @param user boats's owner
     * @return container (can be empty) with boat
     */
    public Optional<Boat> findByIdAndUser(Long id, User user) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Boat> query = cb.createQuery(Boat.class);
            Root<Boat> root = query.from(Boat.class);
            query.select(root)
                    .where(cb.and(
//                            cb.equal(root.get(Boat_.user).get(User_.name), user.getName()),
                            cb.equal(root.get(Boat_.owner), user),
                            cb.equal(root.get(Boat_.id), id)));
            return Optional.of(em.createQuery(query).getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
//        try {
//            return Optional.of(em.createQuery("select c from Boat c where c.id = :id and c.owner = :user", Boat.class)
//                    .setParameter("user", user)
//                    .setParameter("id", id)
//                    .getSingleResult());
//        } catch (NoResultException ex) {
//            return Optional.empty();
//        }
    }

    /**
     * Seeks for all user's boats.
     *
     * @param user boats' owner
     * @return list (can be empty) of user's boats
     */
    public List<Boat> findAllByUser(User user) {
//        return em.createQuery("select c from Boat c where c.owner = :user", Boat.class)
//                .setParameter("user", user)
//                .getResultList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boat> query = cb.createQuery(Boat.class);
        Root<Boat> root = query.from(Boat.class);
        query.select(root)
                .where(cb.equal(root.get(Boat_.owner), user));
        return em.createQuery(query).getResultList();

    }

    public List<Boat> findAllByUserAndBoatType(User user, BoatType boatType) {
//        return em.createQuery("select b from Boat b where b.owner = :user and b.boatType = :boatType", Boat.class)
//                .setParameter("user", user)
//                .setParameter("boatType", boatType)
//                .getResultList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boat> query = cb.createQuery(Boat.class);
        Root<Boat> root = query.from(Boat.class);
        query.select(root)
                .where(cb.and(
                        cb.equal(root.get(Boat_.owner), user),
                        cb.equal(root.get(Boat_.boatType), boatType)
                ));
        return em.createQuery(query).getResultList();
    }

    /**
     * Seeks for one boat of selected boatType and with selected id.
     *
     * @param boatType selected boatType
     * @param id         boat's id
     * @return single boat with selected id and boatType if exists
     */
    public Optional<Boat> findByBoatTypeAndId(BoatType boatType, Long id) {
//        try {
//            return Optional.of(em.createQuery("select c from Boat c where c.id = :id and c.boatType = :boatType", Boat.class)
//                    .setParameter("boatType", boatType)
//                    .setParameter("id", id)
//                    .getSingleResult());
//        } catch (NoResultException ex) {
//            return Optional.empty();
//        }
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Boat> query = cb.createQuery(Boat.class);
            Root<Boat> root = query.from(Boat.class);
            query.select(root)
                    .where(cb.and(
                            cb.equal(root.get(Boat_.boatType), boatType),
                            cb.equal(root.get(Boat_.id), id)));
            return Optional.of(em.createQuery(query).getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }





}
