package kr.debop4j.core.reflect;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import kr.debop4j.core.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * kr.debop4j.core.reflect.FieldAccessTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 21
 */
@Slf4j
public class FieldAccessTest extends AbstractTest {

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void nameSetAndGet() {
        FieldAccess access = FieldAccess.get(SomeClass.class);
        SomeClass test = new SomeClass();

        assertEquals(null, test.name);
        access.set(test, "name", "first");
        assertEquals("first", test.name);
        assertEquals("first", access.get(test, "name"));

        assertEquals(0, test.intValue);
        access.set(test, "intValue", 1234);
        assertEquals(1234, test.intValue);
        assertEquals(1234, access.get(test, "intValue"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void indexSetAndGet() {
        FieldAccess access = FieldAccess.get(SomeClass.class);
        SomeClass test = new SomeClass();
        int index;

        assertEquals(null, test.name);
        index = access.getIndex("name");
        access.set(test, index, "first");
        assertEquals("first", test.name);
        assertEquals("first", access.get(test, index));

        index = access.getIndex("intValue");
        assertEquals(0, test.intValue);
        access.set(test, index, 1234);
        assertEquals(1234, test.intValue);
        assertEquals(1234, access.get(test, index));

        assertEquals(false, access.getBoolean(test, access.getIndex("booleanField")));
        access.setBoolean(test, access.getIndex("booleanField"), true);
        assertEquals(true, access.getBoolean(test, access.getIndex("booleanField")));

        assertEquals(0, access.getByte(test, access.getIndex("byteField")));
        access.setByte(test, access.getIndex("byteField"), (byte) 23);
        assertEquals(23, access.getByte(test, access.getIndex("byteField")));

        assertEquals(0, access.getChar(test, access.getIndex("charField")));
        access.setChar(test, access.getIndex("charField"), (char) 53);
        assertEquals(53, access.getChar(test, access.getIndex("charField")));

        assertEquals(0, access.getShort(test, access.getIndex("shortField")));
        access.setShort(test, access.getIndex("shortField"), (short) 123);
        assertEquals(123, access.getShort(test, access.getIndex("shortField")));

        assertEquals(0, access.getInt(test, access.getIndex("intField")));
        access.setInt(test, access.getIndex("intField"), 123);
        assertEquals(123, access.getInt(test, access.getIndex("intField")));

        assertEquals(0, access.getLong(test, access.getIndex("longField")));
        access.setLong(test, access.getIndex("longField"), 123456789l);
        assertEquals(123456789l, access.getLong(test, access.getIndex("longField")));

        assertEquals(0f, access.getFloat(test, access.getIndex("floatField")), 1.0e-10f);
        access.setFloat(test, access.getIndex("floatField"), 1.23f);
        assertEquals(1.23f, access.getFloat(test, access.getIndex("floatField")), 1.0e-10f);

        assertEquals(0d, access.getDouble(test, access.getIndex("doubleField")), 1.0e-10d);
        access.setDouble(test, access.getIndex("doubleField"), 123.456);
        assertEquals(123.456, access.getDouble(test, access.getIndex("doubleField")), 1.0e-10d);
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void emptyClass() {
        FieldAccess access = FieldAccess.get(EmptyClass.class);
        try {
            access.getIndex("name");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected.printStackTrace();
        }
        try {
            access.get(new EmptyClass(), "meow");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected.printStackTrace();
        }
        try {
            access.get(new EmptyClass(), 0);
            fail();
        } catch (IllegalArgumentException expected) {
            // expected.printStackTrace();
        }
        try {
            access.set(new EmptyClass(), "foo", "moo");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected.printStackTrace();
        }
    }

    static public class SomeClass {
        public String name;
        public int intValue;
        protected float test1;
        Float test2;
        private String test3;

        public boolean booleanField;
        public byte byteField;
        public char charField;
        public short shortField;
        public int intField;
        public long longField;
        public float floatField;
        public double doubleField;
    }

    static public class EmptyClass { }
}
