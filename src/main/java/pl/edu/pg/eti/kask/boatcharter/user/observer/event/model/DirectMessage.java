package pl.edu.pg.eti.kask.boatcharter.user.observer.event.model;

import lombok.*;

/**
 * Description of character transfer between users.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class DirectMessage {


    private String message;

    /**
     * Sending user.
     */
    private String fromUsername;

    /**
     * Receiving user.
     */
    private String toUsername;

}
