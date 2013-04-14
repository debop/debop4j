package kr.debop4j.core.json;

import com.google.common.collect.Lists;
import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.User;
import kr.debop4j.core.tools.StringTool;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * kr.debop4j.core.json.JsonSerializerTest
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 5.
 */
public class JsonSerializerTest extends AbstractTest {

    private static final List<IJsonSerializer> serializers =
            Lists.newArrayList(new GsonSerializer(),
                               new JacksonSerializer(),
                               new FastJsonSerializer());

    @Test
    public void serializeTest() {
        User user = User.getUser(999);

        for (int i = 0; i < serializers.size(); i++) {
            IJsonSerializer serializer = serializers.get(i);

            byte[] serializedBytes = serializer.serialize(user);
            User deserialized = serializer.deserialize(serializedBytes, User.class);

            assertEquals(user, deserialized);
            assertEquals(user.getHomeAddress(), deserialized.getHomeAddress());
            assertEquals(user.getOfficeAddress(), deserialized.getOfficeAddress());

            assertEquals(StringTool.listToString(user.getFavoriteMovies()),
                         StringTool.listToString(deserialized.getFavoriteMovies()));

            assertArrayEquals(user.getByteArray(), deserialized.getByteArray());
        }
    }
}
