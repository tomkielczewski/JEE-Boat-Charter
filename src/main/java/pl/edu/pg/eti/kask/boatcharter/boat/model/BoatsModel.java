package pl.edu.pg.eti.kask.boatcharter.boat.model;

import lombok.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents list of boats to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class BoatsModel implements Serializable {

    /**
     * Represents single boat in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Boat {

        /**
         * Unique id identifying boat.
         */
        private Long id;

        /**
         * Name of the boat.
         */
        private String name;

    }

    /**
     * Name of the selected boats.
     */
    @Singular
    private List<Boat> boats;

    /**
     * @return mapper for convenient converting entity object to model object
     */
    public static Function<Collection<pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat>, BoatsModel> entityToModelMapper() {
        return boats -> {
            BoatsModel.BoatsModelBuilder model = BoatsModel.builder();
            boats.stream()
                    .map(boat -> Boat.builder()
                            .id(boat.getId())
                            .name(boat.getName())
                            .build())
                    .forEach(model::boat);
            return model.build();
        };
    }

}
