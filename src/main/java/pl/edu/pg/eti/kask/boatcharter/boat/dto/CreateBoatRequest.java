package pl.edu.pg.eti.kask.boatcharter.boat.dto;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;

import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateBoatRequest {

    private Long id;
    /**
     * Boat's name.
     */
    private String name;

    /**
     * Type of boat
     */
    private Long boatType;

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
     * @param boatTypeFunction function for converting boat type name to BoatType entity object
     * @param userSupplier       supplier for providing new boat owner
     * @return mapper for convenient converting dto object to entity object
     */
    public static Function<CreateBoatRequest, Boat> dtoToEntityMapper(
//            Function<Long, BoatType> boatTypeFunction,
//            Supplier<User> userSupplier
    ) {
        return request -> Boat.builder()
                .id(request.getId())
                .name(request.getName())
                .numOfSails(request.getNumOfSails())
                .engine(request.getEngine())
                .fuel(request.getFuel())
                .water(request.getWater())
                .year(request.getYear())
                .people(request.getPeople())
                .beam(request.getBeam())
                .length(request.getLength())
                .draught(request.getDraught())
//                .owner(userSupplier.get())
//                .boatType(boatTypeFunction.apply(request.getBoatType()))
                .build();
    }

}
