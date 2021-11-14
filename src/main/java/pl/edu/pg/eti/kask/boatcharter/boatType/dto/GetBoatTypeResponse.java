package pl.edu.pg.eti.kask.boatcharter.boatType.dto;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;

import java.util.function.Function;

/**
 * GET boatType response. Described details about selected boatType. Can be used to present description while
 * character creation or on character's stat page. How boatType is described is defined in
 * {@link BoatType}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetBoatTypeResponse {

    /**
     * Unique id identifying boat.
     */
    private Long id;

    /**
     * Name of the boatType.
     */
    private String name;


    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<BoatType, GetBoatTypeResponse> entityToDtoMapper() {
        return boatType -> {
            GetBoatTypeResponse.GetBoatTypeResponseBuilder response = GetBoatTypeResponse.builder();
            response.name(boatType.getName());
            return response.build();
        };
    }

}
