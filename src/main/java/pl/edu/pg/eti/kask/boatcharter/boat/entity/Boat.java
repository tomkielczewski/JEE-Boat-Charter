package pl.edu.pg.eti.kask.boatcharter.boat.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.watercraft.entity.Watercraft;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity for game creature. Represents all creatures that can be found in the game as well as is base class for are
 * character classes and possible NPCs.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "boats")
public class Boat extends Watercraft {


    /**
     * Owner of the boat.
     */
    @ManyToOne
    @JoinColumn(name ="owner")
    private User owner;

    /**
     * Type of boat
     */
    @ManyToOne
    @JoinColumn(name = "boatType")
    private BoatType boatType;

    /**
     * Number of sails.
     */
    private int numOfSails;

    /**
     * Boat's engine power (hp).
     */
    private int engine;

    /**
     * Boat's fuel tank velocity.
     */
    private int fuel;

    /**
     * Boat's water tank velocity.
     */
    private int water;

    /**
     * Boat's picture. Images in database are stored as blobs (binary large objects).
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] picture;

    /**
     * This method is required due the bug in EclipseLink: https://www.eclipse.org/forums/index.php/t/820662/
     */
    @PrePersist
    @Override
    public void updateCreationDateTime() {
        super.updateCreationDateTime();
    }

    @PreUpdate
    @Override
    public void updateUpdateDateTime() {
        super.updateUpdateDateTime();
    }

}
