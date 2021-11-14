package pl.edu.pg.eti.kask.boatcharter.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * GET users response. Contains user names of users in the system. User name ios the same as login.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUsersResponse {


    private List<GetUserResponse> users;

    public static Function<List<User>, GetUsersResponse> entityToDtoMapper() {
        return users -> GetUsersResponse.builder()
                .users(users.stream()
                        .map(user -> GetUserResponse.entityToDtoMapper().apply(user))
                        .collect(Collectors.toList()))
                .build();
    }

}
