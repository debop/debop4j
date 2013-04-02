package kr.debop4j.data.ogm.test.type;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import kr.debop4j.data.ogm.test.utils.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.ogm.grid.EntityKeyMetadata;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * kr.debop4j.data.ogm.test.type.BuiltInTypeTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 5:34
 */
@Slf4j
public class BuiltInTypeTest extends OgmTestBase {

    private static final Random RANDOM = new Random();
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS Z");

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{ Bookmark.class };
    }

    @Test
    public void testTypesSupport() throws Exception {
        final Session session = openSession();

        Transaction transaction = session.beginTransaction();
        Bookmark b = new Bookmark();
        b.setId("42");
        b.setDescription("Hibernate Site");
        b.setUrl(new URL("http://www.hibernate.org/"));
        BigDecimal weight = new BigDecimal("21.77");
        b.setSiteWeight(weight);
        BigInteger visitCount = new BigInteger("444");
        b.setVisitCount(visitCount);
        b.setIsFavorite(Boolean.TRUE);
        Byte displayMask = Byte.valueOf((byte) '8');
        b.setDisplayMask(displayMask);
        Date now = new Date(System.currentTimeMillis());
        b.setCreationDate(now);
        b.setDestructionDate(now);
        b.setUpdateDate(now);
        final Calendar iCal = Calendar.getInstance();
        iCal.setTimeInMillis(now.getTime());
        b.setCreationCalendar(iCal);
        b.setDestructionCalendar(iCal);
        byte[] blob = new byte[5];
        blob[0] = '1';
        blob[1] = '2';
        blob[2] = '3';
        blob[3] = '4';
        blob[4] = '5';
        b.setBlob(blob);
        UUID serialNumber = UUID.randomUUID();
        b.setSerialNumber(serialNumber);
        final Long userId = RANDOM.nextLong();
        log.info("User ID created: " + userId);
        b.setUserId(userId);
        final Integer stockCount = Integer.valueOf(RANDOM.nextInt());
        b.setStockCount(stockCount);

        session.persist(b);
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        b = (Bookmark) session.get(Bookmark.class, b.getId());
        assertEquals("http://www.hibernate.org/", b.getUrl().toString());
        assertEquals(weight, b.getSiteWeight());
        assertEquals(visitCount, b.getVisitCount());
        assertEquals(Boolean.TRUE, b.getIsFavorite());
        assertEquals(displayMask, b.getDisplayMask());
        assertEquals("serial number incorrect", serialNumber, b.getSerialNumber());
        assertEquals("user id incorrect", userId, b.getUserId());
        assertEquals("stock count incorrect", stockCount, b.getStockCount());

        assertEquals("Creation Date Incorrect", now, b.getCreationDate());

        assertEquals("Timezone Info Not Correct", iCal.getTimeZone(), b.getDestructionCalendar().getTimeZone());

        assertEquals("Date info String Not Correct iCal",
                     iCal.getTime().toGMTString(), b.getDestructionCalendar().getTime().toGMTString());

        // This test can break in ehcache dialect.
        assertEquals("Timezone Info Not Correct", iCal.getTimeZone(), b.getDestructionCalendar().getTimeZone());

        assertEquals("Date info Not Correct iCal: "
                             + DATE_FORMAT.format(iCal.getTime())
                             + " dest millis: " + b.getDestructionCalendar().getTimeInMillis()
                             + " iCal millis: " + iCal.getTimeInMillis(),
                     iCal.getTime(), b.getDestructionCalendar().getTime());

        assertEquals("Byte array incorrect length", blob.length, b.getBlob().length);
        assertEquals(blob[0], b.getBlob()[0]);
        assertEquals('1', b.getBlob()[0]);
        assertEquals('2', b.getBlob()[1]);
        assertEquals('3', b.getBlob()[2]);
        assertEquals('4', b.getBlob()[3]);
        assertEquals('5', b.getBlob()[4]);

        session.delete(b);
        transaction.commit();

        session.close();
    }

    @Test
    public void testStringMappedTypeSerialisation() throws Exception {
        final Session session = openSession();
        Transaction transaction = session.beginTransaction();

        Bookmark b = new Bookmark();
        b.setId("42");
        b.setUrl(new URL("http://www.hibernate.org/"));
        BigDecimal weight = new BigDecimal("21.77");
        b.setSiteWeight(weight);
        BigInteger visitCount = new BigInteger("444");
        b.setVisitCount(visitCount);
        UUID serialNumber = UUID.randomUUID();
        b.setSerialNumber(serialNumber);
        final Long userId = RANDOM.nextLong();
        b.setUserId(userId);
        final Integer stockCount = Integer.valueOf(RANDOM.nextInt());
        b.setStockCount(stockCount);

        session.persist(b);
        transaction.commit();
        session.clear();

        transaction = session.beginTransaction();
        b = (Bookmark) session.get(Bookmark.class, b.getId());

        //Check directly in the cache the values stored
        EntityKeyMetadata keyMetadata = new EntityKeyMetadata("Bookmark", new String[]{ "id" });
        EntityKey key = new EntityKey(keyMetadata, new Object[]{ "42" });
        Map<String, String> entity = (Map<String, String>) TestHelper.extractEntityTuple(sessions, key);

        assertEquals("Entity visits count incorrect", entity.get("visits_count"), "444");
        assertEquals("Entity serial number incorrect", entity.get("serialNumber"), serialNumber.toString());
        assertEquals("Entity URL incorrect", entity.get("url"), "http://www.hibernate.org/");
        assertEquals("Entity site weight incorrect", entity.get("site_weight"), "21.77");

        session.delete(b);
        transaction.commit();
        session.close();
    }
}
