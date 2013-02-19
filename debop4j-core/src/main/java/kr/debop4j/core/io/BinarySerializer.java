package kr.debop4j.core.io;

import kr.debop4j.core.ISerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 객체를 메모리 덤프를 통해 직렬화를 수행합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 10. 4.
 */
@Slf4j
public class BinarySerializer implements ISerializer {

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] serialize(Object graph) {

        if (graph == null)
            return new byte[0];

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(graph);
            oos.flush();

            return bos.toByteArray();
        } catch (Exception e) {
            log.error("객체정보를 직렬화하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {

        if (bytes == null || bytes.length == 0)
            return null;

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            log.error("객체정보를 역직렬화하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }
}
