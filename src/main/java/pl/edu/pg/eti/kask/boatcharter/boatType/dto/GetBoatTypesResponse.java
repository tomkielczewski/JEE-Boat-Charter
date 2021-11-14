package pl.edu.pg.eti.kask.boatcharter.boatType.dto;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

/**
 * GET boatTypes response. Returns list of all available boatTypes names.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetBoatTypesResponse {


    /**
     * List of all boatTypes names.
     */
    @Singular
    private List<String> boatTypes;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<BoatType>, GetBoatTypesResponse> entityToDtoMapper() {
        return boatTypes -> {
            GetBoatTypesResponse.GetBoatTypesResponseBuilder response = GetBoatTypesResponse.builder();
            boatTypes.stream()
                    .map(BoatType::getName)
                    .forEach(response::boatType);
            return response.build();
        };
    }

}
