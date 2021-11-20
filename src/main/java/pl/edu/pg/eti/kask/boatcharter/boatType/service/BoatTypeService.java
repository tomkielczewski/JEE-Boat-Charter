package pl.edu.pg.eti.kask.boatcharter.boatType.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.boatType.repository.BoatTypeRepository;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding character's boatType entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor
//@RolesAllowed(UserRoles.USER)
public class BoatTypeService {

    /**
     * Repository for boatType entity.
     */
    private BoatTypeRepository repository;

    /**
     * @param repository repository for boatType entity
     */
    @Inject
    public BoatTypeService(BoatTypeRepository repository) {
        this.repository = repository;
    }

//    /**
//     * @param name name of the boatType
//     * @return container with boatType entity
//     */
//    public Optional<BoatType> find(String name) {
//        return repository.find(name);
//    }

    /**
     * @param id id of the boatType
     * @return container with boatType entity
     */
    public Optional<BoatType> find(Long id) {
        return repository.find(id);
    }

    /**
     * Find all boatTypes.
     *
     * @return all available boatTypes
     */
    public List<BoatType> findAll() {
        return repository.findAll();
    }

    /**
     * Stores new boatType in the data store.
     *
     * @param boatType new boatType to be saved
     */
    public void create(BoatType boatType) {
        repository.create(boatType);
    }

    public void delete(Long boatTypeId) {
        BoatType boatType = repository.find(boatTypeId).orElseThrow();
        repository.delete(boatType);
    }

    public void update(BoatType boatType) {
        repository.update(boatType);
    }

    public void deleteAll() { repository.deleteAll(); }

}
