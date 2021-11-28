package pl.edu.pg.eti.kask.boatcharter.push.dto;

import lombok.*;

/**
 * WebSocket message representation.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class Message {

    /**
     * DirectMessage author.
     */
    private String from;

    /**
     * DirectMessage content.
     */
    private String content;

}
