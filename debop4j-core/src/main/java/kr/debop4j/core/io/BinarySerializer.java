/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.core.io;

import kr.debop4j.core.ISerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 객체를 메모리 덤프를 통해 직렬화를 수행합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 10. 4.
 */
@Slf4j
public class BinarySerializer implements ISerializer {

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

    /**
     * 입력 스트림을 읽어 객체로 빌드합니다.
     *
     * @param inputStream 입력 스트림
     * @param clazz       대상 객체 수형
     * @param <T>         객체 수형
     * @return 객체 인스턴스
     */
    @SuppressWarnings( "unchecked" )
    public <T> T readObject(InputStream inputStream, Class<T> clazz) {
        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            log.error("InputStream으로부터 정보를 읽는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체를 output stream 에 씁니다.
     *
     * @param graph        대상 객체
     * @param outputStream output stream
     */
    public void writeObject(Object graph, OutputStream outputStream) {
        try (ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
            oos.writeObject(graph);
            oos.flush();
        } catch (Exception e) {
            log.error("객체정보를 직렬화하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }
}
