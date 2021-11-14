package pl.edu.pg.eti.kask.boatcharter.boatType.entity;

import lombok.*;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "boatTypes")
public class BoatType implements Serializable {

    @Id
    private Long id;

    /**
     * Name of the boat type.
     */
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "boatType", cascade = CascadeType.REMOVE)
    private List<Boat> boats;

}
