package pl.edu.pg.eti.kask.boatcharter.boatType.dto;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateBoatTypeRequest {

    private Long id;

    private String name;

    private List<Boat> boats;

    public static Function<CreateBoatTypeRequest, BoatType> dtoToEntityMapper() {
        return request -> BoatType.builder()
                .id(request.getId())
                .name(request.getName())

                .boats(request.getBoats())
                .build();
    }
}
