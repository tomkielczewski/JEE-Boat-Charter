package pl.edu.pg.eti.kask.boatcharter.boatType.model;

import lombok.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents list of boatTypes to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class BoatTypesModel implements Serializable {

    /**
     * Represents single boatType in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class BoatType {

        private Long id;
        /**
         * Name of the boatType.
         */
        private String name;

    }

    /**
     * Name of the selected boatTypes.
     */
    @Singular
    private List<BoatType> boatTypes;

    /**
     * @return mapper for convenient converting entity object to model object
     */
    public static Function<Collection<pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType>, BoatTypesModel> entityToModelMapper() {
        return boatTypes -> {
            BoatTypesModel.BoatTypesModelBuilder model = BoatTypesModel.builder();
            boatTypes.stream()
                    .map(boatType -> BoatType.builder()
                            .id(boatType.getId())
                            .name(boatType.getName())
                            .build())
                    .forEach(model::boatType);
            return model.build();
        };
    }

}
