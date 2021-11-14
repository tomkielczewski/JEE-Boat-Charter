package pl.edu.pg.eti.kask.boatcharter.boatType.model;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;

import java.util.List;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents single boatType to be displayed or selected.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class BoatTypeModel {


    private Long id;

    /**
     * Name of the boatType.
     */
    private String name;

    private List<Boat> boats;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<BoatType, BoatTypeModel> entityToModelMapper() {
        return boatType -> BoatTypeModel.builder()
                .id(boatType.getId())
                .name(boatType.getName())
                .boats(boatType.getBoats())
                .build();
    }
}
