package pl.edu.pg.eti.kask.boatcharter.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * PUT password request. Usually there are seperate forms for updating user profile (name, etc.) and password.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdatePasswordRequest {

    /**
     * New value for user's passowrd.
     */
    private String password;

}
