package kr.debop4j.data.mapping.annotated.collection;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.mapping.model.annotated.collection.Car;
import kr.debop4j.data.mapping.model.annotated.collection.CarOption;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;

/**
 * org.annotated.mapping.domain.model.collection.CarTest
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 15.
 */
public class CarTest {

    private static SessionFactory sessionFactory;

    private Session session;

    @BeforeClass
    public static void beforeClass() {
        if (Springs.isNotInitialized())
            Springs.init("applicationContext.xml");

        sessionFactory = Springs.getBean(SessionFactory.class);
    }

    @Before
    public void before() {
        session = sessionFactory.openSession();
    }

    @After
    public void after() {
        if (session != null) {
            session.close();
            session = null;
        }
    }


    @Test
    public void mapAssociation() throws Exception {

        Car merc = new Car();
        merc.getOptions().put("AIRCO", "DUAL-AUTO");
        merc.getOptions().put("GEARBOX", "AUTO");
        merc.getOptions().put("CUPHOLDER", "YES");

        session.save(merc);
        session.flush();
        session.evict(merc);

        Car loaded = (Car) session.get(Car.class, merc.getId());
        Assert.assertEquals(3, loaded.getOptions().size());
    }

    @Test
    public void mapAssociationWithNull() throws Exception {

        Car bmw = new Car();
        bmw.getOptions().put("AIRCO", "DUAL-AUTO");
        bmw.getOptions().put("GEARBOX", "AUTO");
        bmw.getOptions().put("CUPHOLDER", null);

        session.save(bmw);
        session.flush();
        session.evict(bmw);

        Car loaded = (Car) session.get(Car.class, bmw.getId());
        // NULL 값을 가진 OPTION 은 저장되지 않는다. 그러므로 OPTION 은 2개입니다.
        Assert.assertEquals(2, loaded.getOptions().size());
    }

    @Test
    public void mapAssociationWithClass() throws Exception {

        Car bmw = new Car();
        bmw.getCarOptions().put("AIRCO", new CarOption("AIRCO-2", 100));
        bmw.getCarOptions().put("GEARBOX", new CarOption("GEARBOX-2", 200));

        session.save(bmw);
        session.flush();
        session.evict(bmw);

        Car loaded = (Car) session.get(Car.class, bmw.getId());
        Assert.assertEquals(2, loaded.getCarOptions().size());
    }
}
