package kr.debop4j.core.json;

import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static kr.debop4j.core.tools.StringTool.listToString;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * kr.debop4j.core.json.JsonToolTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 5.
 */
@Slf4j
public class JsonToolTest extends AbstractTest {

    @Test
    public void serializeToBytes() {

        User user = User.getUser(999);

        byte[] serializedBytes = JsonTool.serializeAsBytes(user);
        User deserialized = JsonTool.deserializeFromBytes(serializedBytes, User.class);

        assertEquals(user, deserialized);
        assertEquals(user.getHomeAddress(), deserialized.getHomeAddress());
        assertEquals(user.getOfficeAddress(), deserialized.getOfficeAddress());
        assertEquals(listToString(user.getFavoriteMovies()), listToString(deserialized.getFavoriteMovies()));

        assertArrayEquals(user.getByteArray(), deserialized.getByteArray());
    }

    @Test
    public void serializeToText() {

        User user = User.getUser(999);

        String serializedText = JsonTool.serializeAsText(user);
        User deserialized = JsonTool.deserializeFromText(serializedText, User.class);

        assertEquals(user, deserialized);
        assertEquals(user.getHomeAddress(), deserialized.getHomeAddress());
        assertEquals(user.getOfficeAddress(), deserialized.getOfficeAddress());
        assertEquals(listToString(user.getFavoriteMovies()), listToString(deserialized.getFavoriteMovies()));

        assertArrayEquals(user.getByteArray(), deserialized.getByteArray());
    }
}
