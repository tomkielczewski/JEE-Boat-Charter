package pl.edu.pg.eti.kask.boatcharter.boat.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.model.BoatsModel;
import pl.edu.pg.eti.kask.boatcharter.boat.service.interceptor.binding.CrudLogger;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.boat.repository.BoatRepository;
import pl.edu.pg.eti.kask.boatcharter.boatType.repository.BoatTypeRepository;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;
import pl.edu.pg.eti.kask.boatcharter.user.entity.UserRoles;
import pl.edu.pg.eti.kask.boatcharter.user.observer.event.BroadcastMessageEvent;
import pl.edu.pg.eti.kask.boatcharter.user.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service layer for all business actions regarding boat entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor
@RolesAllowed(UserRoles.USER)
public class BoatService {

//    private static final CrudLogger log = CrudLogger.getLogger( BoatService.class.getName() );

    /**
     * Repository for boat entity.
     */
    private BoatRepository repository;

    private BoatTypeRepository boatTypeRepository;

    private UserRepository userRepository;

    private SecurityContext securityContext;

    private Event<String> broadcastMessageEvent;


    /**
     * @param repository repository for boat entity
     */
    @Inject
    public BoatService(@BroadcastMessageEvent Event<String> broadcastMessageEvent, BoatRepository repository, BoatTypeRepository boatTypeRepository, UserRepository userRepository, SecurityContext securityContext) {
        this.broadcastMessageEvent = broadcastMessageEvent;
        this.repository = repository;
        this.boatTypeRepository = boatTypeRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    /**
     * Finds single boat.
     *
     * @param id boat's id
     * @return container with boat
     */
//    public Optional<Boat> find(Long id) {
//        return repository.find(id);
//    }

    public Optional<Boat> find(Long id) {
        broadcastMessageEvent.fire("Find boat: " + id);
        Optional<Boat> boat = null;
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            boat = repository.find(id);
        }
        else if (securityContext.isCallerInRole(UserRoles.USER)) {
            boat = this.findForCallerPrincipal(id);
        }
        return boat;
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
     * @param login username
     * @return container (may be empty if no such user) with list of boats
     */
    public Optional<List<Boat>> findAllByUser(String login) {
        Optional<User> user = userRepository.find(login);
        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(repository.findAllByUser(user.get()));
        }
    }

    public Optional<List<Boat>> findAllByUserAndBoatType(String login, BoatType boatType) {
        Optional<User> user = userRepository.find(login);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(repository.findAllByUserAndBoatType(user.get(), boatType));
        }
    }

    /**
     * @param login username
     * @param id    boat's id
     * @return container, may be empty if no such user or no boat with specified user nad id
     */
    public Optional<Boat> findByUser(String login, Long id) {
        Optional<User> user = userRepository.find(login);
        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            return repository.findByIdAndUser(id, user.get());
        }
    }

    /**
     * @return all available boats of the authenticated user
     */
    public Optional<Boat> findForCallerPrincipal(Long id) {
        return repository.findByIdAndUser(id, userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow());
    }


    /**tName()).orElseThrow(), boatType);
//    }
     * @return all available boats of the authenticated user
     */
    public List<Boat> findAllForCallerPrincipal() {
        return repository.findAllByUser(userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow());
    }

//    public List<Boat> findAllByBoatTypeForCallerPrincipal(BoatType boatType) {
//        return repository.findAllByUserAndBoatType(userRepository.find(securityContext.getCallerPrincipal().ge

    public List<Boat> findAllByBoatTypeForCallerPrincipal(BoatType boatType) {
        List<Boat> boat = null;
        if (securityContext.isCallerInRole(UserRoles.ADMIN)){
            boat =  repository.findAllByType(boatType);
        }
        else {
            boat =  repository.findAllByUserAndBoatType(userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow(), boatType);
        };
        return boat;
    }

    /**
     * Assigns currently logged user to passed new boat and saves it in data store,
     *
     * @param boat new boat to be saved
     */
    @CrudLogger
    public void createForCallerPrincipal(Boat boat) {
        User user = userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow();
        boat.setOwner(user);
        user.getBoats().add(boat);
        repository.create(boat);
    }



    /**
     * Creates new boat.
     *
     * @param boat new boat
     */
    @CrudLogger
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
    @CrudLogger
    public void update(Boat boat) {
        repository.update(boat);
    }

    /**
     * Deletes existing boat.
     *
     * @param boatId existing boat's id to be deleted
     */
    @CrudLogger
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

    @CrudLogger
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
