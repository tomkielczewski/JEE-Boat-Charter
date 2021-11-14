package pl.edu.pg.eti.kask.boatcharter.boat.dto;

import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * GET boats response. Contains list of available boats. Can be used to list particular user's boats as
 * well as all boats in the game.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetBoatsResponse {

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
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat>, GetBoatsResponse> entityToDtoMapper() {
        return boats -> {
            GetBoatsResponse.GetBoatsResponseBuilder response = GetBoatsResponse.builder();
            boats.stream()
                    .map(boat -> Boat.builder()
                            .id(boat.getId())
                            .name(boat.getName())
                            .build())
                    .forEach(response::boat);
            return response.build();
        };
    }

}
