package pl.edu.pg.eti.kask.boatcharter.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * PUT user request. Contains only fields which can be changed byt the user while updating its profile. User is defined
 * in {@link pl.edu.pg.eti.kask.boatcharter.user.entity.User}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateUserRequest {

    /**
     * User's name.
     */
    private String name;

    /**
     * User's surname.
     */
    private String surname;

    /**
     * User's birth day.
     */
    private LocalDate birthDate;

    /**
     * User's email.
     */
    private String email;

}
