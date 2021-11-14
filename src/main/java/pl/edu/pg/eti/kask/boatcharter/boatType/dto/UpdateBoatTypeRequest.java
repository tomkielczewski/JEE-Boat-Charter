package pl.edu.pg.eti.kask.boatcharter.boatType.dto;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateBoatTypeRequest {
    private Long id;

    private String name;

    private int numOfEmployees;

    private String city;

    private String establishDate;

    private List<Boat> boats;

    public static BiFunction<BoatType, UpdateBoatTypeRequest, BoatType> dtoToEntityUpdater() {
        return (boatType, request) -> {
            if(request.getName() != null){
                boatType.setName(request.getName());
            }
            if(request.getBoats() != null){
                boatType.setBoats(request.getBoats());
            }
            return boatType;
        };
    }
}
