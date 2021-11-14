package pl.edu.pg.eti.kask.boatcharter.boat.dto;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;

import java.util.function.Function;

/**
 * GET boat response. It contains all field that can be presented (but not necessarily changed) to the used. How
 * boat is described is defined in {@link pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat} and
 * {@link pl.edu.pg.eti.kask.boatcharter.watercraft.entity.Watercraft} classes.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetBoatResponse {

    /**
     * Unique id (primary key).
     */
    private Long id;

    /**
     * Boat's name.
     */
    private String name;

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

    /**
     * Type of boat
     */
    private String boatType;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Boat, GetBoatResponse> entityToDtoMapper() {
        return boat -> GetBoatResponse.builder()
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
                .boatType(boat.getBoatType().getName())
                .build();
    }



}
