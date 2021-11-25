package pl.edu.pg.eti.kask.boatcharter.user.model;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;

import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents single user to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UserModel {

    /**
     * Username.
     */
    private String login;


    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<User, UserModel> entityToModelMapper() {
        return user -> UserModel.builder()
                .login(user.getLogin())
                .build();
    }
}
