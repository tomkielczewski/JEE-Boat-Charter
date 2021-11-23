package pl.edu.pg.eti.kask.boatcharter.configuration;

        import lombok.SneakyThrows;
        import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
        import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
        import pl.edu.pg.eti.kask.boatcharter.digest.Sha256Utility;
        import pl.edu.pg.eti.kask.boatcharter.user.entity.User;
        import pl.edu.pg.eti.kask.boatcharter.user.entity.UserRoles;

        import javax.annotation.PostConstruct;
        import javax.ejb.Singleton;
        import javax.ejb.Startup;
        import javax.inject.Inject;
        import javax.persistence.EntityManager;
        import javax.persistence.PersistenceContext;
        import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.time.LocalDate;
        import java.util.List;

/**
 * Listener started automatically on CDI application context initialized. Injects proxy to the services and fills
 * database with default content. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
public class InitializeData {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * Password hashing algorithm.
     */
    private Pbkdf2PasswordHash pbkdf;

    @Inject
    public InitializeData(Pbkdf2PasswordHash pbkdf) {
        this.pbkdf = pbkdf;
    }

    public InitializeData() {
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should
     * be created only once.
     */
    @PostConstruct
    private synchronized void init() {
        User admin = User.builder()
                .login("admin")
                .name("Adam")
                .surname("Cormel")
                .birthDate(LocalDate.of(1990, 10, 21))
                .email("admin@simplerpg.example.com")
                .password(pbkdf.generate("adminadmin".toCharArray()))
                .roles(List.of(UserRoles.ADMIN, UserRoles.USER))
                .build();

        User kevin = User.builder()
                .login("kevin")
                .name("Kevin")
                .surname("Pear")
                .birthDate(LocalDate.of(2001, 1, 16))
                .email("kevin@example.com")
                .password(pbkdf.generate("useruser".toCharArray()))
                .roles(List.of(UserRoles.USER))
                .avatar(getResourceAsByteArray("avatar/uhlbrecht.png"))//package relative path
                .build();

        User alice = User.builder()
                .login("alice")
                .name("Alice")
                .surname("Grape")
                .birthDate(LocalDate.of(2002, 3, 19))
                .email("alice@example.com")
                .password(pbkdf.generate("useruser".toCharArray()))
                .roles(List.of(UserRoles.USER))
                .build();

        em.persist(admin);
        em.persist(kevin);
        em.persist(alice);

        BoatType sailboat = BoatType.builder().id(Long.valueOf(1)).name("Sailboat").build();
        BoatType houseboat = BoatType.builder().id(Long.valueOf(2)).name("Houseboat").build();
        BoatType catamaran = BoatType.builder().id(Long.valueOf(3)).name("Catamaran").build();
        BoatType luxury = BoatType.builder().id(Long.valueOf(4)).name("LuxuryYacht").build();

        em.persist(sailboat);
        em.persist(houseboat);
        em.persist(catamaran);
        em.persist(luxury);

        Boat bavaria = Boat.builder()
                .name("Bavaria")
                .boatType(sailboat)
                .numOfSails(2)
                .engine(55)
                .fuel(210)
                .water(360)
                .year(2017)
                .people(9)
                .beam((float) 4.35)
                .length((float) 14.4)
                .draught((float) 2.10)
                .picture(getResourceAsByteArray("picture/sailboat.jpg"))//package relative path
                .owner(kevin)
                .build();

        Boat azimut = Boat.builder()
                .name("Azimut")
                .boatType(houseboat)
                .numOfSails(0)
                .engine(3000)
                .fuel(4700)
                .water(570)
                .year(2003)
                .people(6)
                .beam((float) 4.87)
                .length((float) 17.37)
                .draught((float) 1.52)
                .picture(getResourceAsByteArray("picture/houseboat.jpg"))//package relative path
                .owner(alice)
                .build();

        Boat hanse = Boat.builder()
                .name("Hanse")
                .boatType(sailboat)
                .numOfSails(2)
                .engine(38)
                .fuel(160)
                .water(320)
                .year(2020)
                .people(6)
                .beam((float) 4.17)
                .length((float) 12.4)
                .draught((float) 2.1)
                .picture(getResourceAsByteArray("picture/sailboat.jpg"))//package relative path
                .owner(alice)
                .build();

        em.persist(bavaria);
        em.persist(azimut);
        em.persist(hanse);

        kevin.setBoats(List.of(bavaria));
        alice.setBoats(List.of(azimut, hanse));

    }

    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        Path filePath = Paths.get(AvatarsFolder.AVATARS_FOLDER, name);
        return Files.readAllBytes(filePath);
//        try (InputStream is = this.getClass().getResourceAsStream(name)) {
//            return is.readAllBytes();
//        }
    }

}
