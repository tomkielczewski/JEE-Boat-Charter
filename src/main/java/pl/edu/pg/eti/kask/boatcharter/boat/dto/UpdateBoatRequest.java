package pl.edu.pg.eti.kask.boatcharter.boat.dto;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;

import java.util.List;
import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateBoatRequest {
    private Long id;

    private User owner;

    private BoatType boatType;

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


    public static BiFunction<Boat, UpdateBoatRequest, Boat> dtoToEntityUpdater() {
        return (boat, request) -> {
            if(boat.getName() != null){
                boat.setName(request.getName());
            }
            if(request.getNumOfSails() >= 0){
                boat.setNumOfSails(request.getNumOfSails());
            }
            if(request.getEngine() >= 0){
                boat.setEngine(request.getEngine());
            }
            if(request.getFuel()  >= 0){
                boat.setFuel(request.getFuel());
            }
            if(request.getWater()  >= 0){
                boat.setWater(request.getWater());
            }
            if(request.getYear()  >= 1900){
                boat.setYear(request.getYear());
            }
            if(request.getPeople()  > 0){
                boat.setPeople(request.getPeople());
            }
            if(request.getBeam()  > 0){
                boat.setBeam(request.getBeam());
            }
            if(request.getLength()  > 0){
                boat.setLength(request.getLength());
            }
            if(request.getDraught()  > 0){
                boat.setDraught(request.getDraught());
            }
            return boat;
        };
    }
}
