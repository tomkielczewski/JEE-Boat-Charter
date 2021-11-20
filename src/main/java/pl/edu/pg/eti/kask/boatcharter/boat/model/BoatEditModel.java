package pl.edu.pg.eti.kask.boatcharter.boat.model;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;

import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class BoatEditModel {

    private String name;

    private int numOfSails;

    private int engine;

    private int fuel;

    private int water;

    private int year;

    private int people;

    private float beam;

    private float length;

    private float draught;

//    private String picture;


    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Boat, BoatEditModel> entityToModelMapper() {
        return boat -> BoatEditModel.builder()
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
//                .boatType(boat.getBoatType().getName())
//                .picture(boat.getPicture())
                .build();
    }

    public static BiFunction<Boat, BoatEditModel, Boat> modelToEntityUpdater() {
        return (boat, request) -> {
            boat.setName(request.getName());
            boat.setNumOfSails(request.getNumOfSails());
            boat.setEngine(request.getEngine());
            boat.setFuel(request.getFuel());
            boat.setWater(request.getWater());
            boat.setYear(request.getYear());
            boat.setPeople(request.getPeople());
            boat.setBeam(request.getBeam());
            boat.setLength(request.getLength());
            boat.setDraught(request.getDraught());
//            boat.setOwner(request.getOwner());
//            boat.setBoatType(request.getBoatType());
//                boat.setPicture("/api/pictures/" + request.getId());
            return boat;
        };
    }
}