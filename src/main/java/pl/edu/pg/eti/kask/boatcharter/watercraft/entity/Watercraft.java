package pl.edu.pg.eti.kask.boatcharter.watercraft.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.boatcharter.entity.VersionAndCreationDateAuditable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity for game creature. Represents all creatures that can be found in the game as well as is base class for are
 * character classes and possible NPCs.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "watercrafts")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Watercraft extends VersionAndCreationDateAuditable implements Serializable {

    /**
     * Unique id (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    /**
     * Watercraft's name.
     */
    private String name;

    /**
     * Year the Watercraft have been build in.
     */
    private int year;

    /**
     * Watercraft's people capacity.
     */
    private int people;

    /**
     * Watercraft's beam width.
     */
    private float beam;

    /**
     * Watercraft's length.
     */
    private float length;

    /**
     * Watercraft's draught.
     */
    private float draught;



}
