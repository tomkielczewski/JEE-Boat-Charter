package pl.edu.pg.eti.kask.boatcharter.boatType.repository;


import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.repository.Repository;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Repository for boatType entity. Repositories should be used in business layer (e.g.: in services).
 */
@Dependent
public class BoatTypeRepository implements Repository<BoatType, Long> {

    /**
     * Connection with the database (not thread safe).
     */
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public Optional<BoatType> find(Long id) {
        return Optional.ofNullable(em.find(BoatType.class, id));
    }


    @Override
    public List<BoatType> findAll() {
//        return em.createQuery("select r from BoatType r", BoatType.class).getResultList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BoatType> query = cb.createQuery(BoatType.class);
        Root<BoatType> root = query.from(BoatType.class);
        query.select(root);
        return em.createQuery(query).getResultList();

    }


    @Override
    public void create(BoatType entity) {
        em.persist(entity);
    }

    @Override
    public void delete(BoatType entity) {
        em.remove(em.find(BoatType.class, entity.getId()));
    }

    @Override
    public void detach(BoatType entity) {
        em.detach(entity);
    }

    public void deleteAll() {
//        em.createQuery("select r from BoatType r", BoatType.class)
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BoatType> query = cb.createQuery(BoatType.class);
        Root<BoatType> root = query.from(BoatType.class);
        query.select(root);
        em.createQuery(query)
                .getResultList()
                .forEach(this::delete);
    }

    @Override
    public void update(BoatType entity) {
        em.merge(entity);
    }
    
}

