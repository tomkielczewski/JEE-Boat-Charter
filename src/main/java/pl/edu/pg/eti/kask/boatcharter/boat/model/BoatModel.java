package pl.edu.pg.eti.kask.boatcharter.boat.model;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;

import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents single character to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class BoatModel {

    /**
     * Boat's unique id.
     */
    private Long id;

    /**
     * Boat's name.
     */
    private String name;

    /**
     * Type of boat
     */
    private String boatType;

    /**
     * Number of sails.
     */
    private int numOfSails;

    /**
     * Boat's engine power (hp).
     */
    private int engine;

    /**
     * Boat's fuel tank velocity.
     */
    private int fuel;

    /**
     * Boat's water tank velocity.
     */
    private int water;

    /**
     * Year the Boat have been build in.
     */
    private int year;

    /**
     * Boat's people capacity.
     */
    private int people;

    /**
     * Boat's beam width.
     */
    private float beam;

    /**
     * Boat's length.
     */
    private float length;

    /**
     * Boat's draught.
     */
    private float draught;

    private User owner;

    /**
     * URL for boats picture.
     */
    private String picture;



    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Boat, BoatModel> entityToModelMapper() {
        return boat -> BoatModel.builder()
                .id(boat.getId())
                .name(boat.getName())
                .numOfSails(boat.getNumOfSails())
                .engine(boat.getEngine())
                .fuel(boat.getFuel())
                .water(boat.getWater())
                .year(boat.getYear())
                .people(boat.getPeople())
                .beam(boat.getBeam())
                .length(boat.getLength())
                .draught(boat.getDraught())
                .owner(boat.getOwner())
                .boatType(boat.getBoatType().getName())
                .picture("/api/pictures/" + boat.getId())
                .build();
    }

}
