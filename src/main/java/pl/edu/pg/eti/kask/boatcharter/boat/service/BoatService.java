package pl.edu.pg.eti.kask.boatcharter.boat.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.boat.repository.BoatRepository;
import pl.edu.pg.eti.kask.boatcharter.boatType.repository.BoatTypeRepository;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding boat entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor
//@RolesAllowed(UserRoles.USER)
public class BoatService {

    /**
     * Repository for boat entity.
     */
    private BoatRepository repository;

    private BoatTypeRepository boatTypeRepository;
    /**
     * @param repository repository for boat entity
     */
    @Inject
    public BoatService(BoatRepository repository, BoatTypeRepository boatTypeRepository) {
        this.repository = repository;
        this.boatTypeRepository = boatTypeRepository;
    }

    /**
     * Finds single boat.
     *
     * @param id boat's id
     * @return container with boat
     */
    public Optional<Boat> find(Long id) {
        return repository.find(id);
    }

    /**
     * @param id   boat's id
     * @param user existing user
     * @return selected boat for user
     */
    public Optional<Boat> find(User user, Long id) {
        return repository.findByIdAndUser(id, user);
    }

    /**
     * @return all available boats
     */
    public List<Boat> findAll() {
        return repository.findAll();
    }

    /**
     * @param user existing user, boat's owner
     * @return all available boats of the selected user
     */
    public List<Boat> findAll(User user) {
        return repository.findAllByUser(user);
    }

    /**
     * @param boatType boat's Type
     * @return all available boats of the selected BoatType
     */
//    findAllBoatsByType
    public List<Boat> findAll(BoatType boatType) {
        return repository.findAllByType(boatType);
    }


    /**
     * Creates new boat.
     *
     * @param boat new boat
     */
    public void create(Boat boat) {
        repository.create(boat);
        boatTypeRepository.find(boat.getBoatType().getId()).ifPresent(boatType -> {
            boatType.getBoats().add(boat);
        });
    }

    /**
     * Updates existing boat.
     *
     * @param boat boat to be updated
     */
    public void update(Boat boat) {
        repository.update(boat);
    }

    /**
     * Deletes existing boat.
     *
     * @param boatId existing boat's id to be deleted
     */
    public void delete(Long boatId) {
        Boat boat = repository.find(boatId).orElseThrow();
        User owner = boat.getOwner();
        if (owner != null) {
            owner.getBoats().remove(boat);
        }
        BoatType boatType = boat.getBoatType();
        boatType.getBoats().remove(boat);
        repository.delete(boat);
    }

    public void delete(BoatType boatType) {
        List<Boat> boats = repository.findAllByType(boatType);
        boats.forEach(boat -> {
            User owner = boat.getOwner();
            if (owner != null) {
                owner.getBoats().remove(boat);
            }
            boatType.getBoats().remove(boat);
        });
        repository.deleteByBoatType(boatType);
    }

    public void deleteAll() { repository.deleteAll(); }

    /**
     * Updates picture of the boat.
     *
     * @param id boat's id
     * @param is input stream containing new portrait
     */
    public void updatePicture(Long id, InputStream is) {
        repository.find(id).ifPresent(boat -> {
            try {
                boat.setPicture(is.readAllBytes());
                repository.update(boat);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

}
