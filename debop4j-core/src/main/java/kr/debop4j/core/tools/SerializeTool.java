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

package kr.debop4j.core.tools;

import kr.debop4j.core.BinaryStringFormat;
import kr.debop4j.core.ISerializer;
import kr.debop4j.core.io.BinarySerializer;
import kr.debop4j.core.parallelism.AsyncTool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static kr.debop4j.core.Guard.shouldNotBeNull;
import static kr.debop4j.core.tools.StringTool.getBytesFromHexString;
import static kr.debop4j.core.tools.StringTool.getStringFromBytes;

/**
 * {@link ISerializer} 를 이용한 직렬화/역직렬화를 수행하는 Utility Method 를 제공합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 14
 */
@Slf4j
public final class SerializeTool {

    @Getter( lazy = true )
    private static final BinarySerializer binarySerializer = new BinarySerializer();

    private SerializeTool() { }

    /**
     * 객체를 직렬화하여 문자열로 반환합니다.
     *
     * @param serializer the serializer
     * @param graph      the graph
     * @return the string
     */
    public static String serializeAsString(ISerializer serializer, Object graph) {
        shouldNotBeNull(serializer, "serializer");
        if (graph == null)
            return StringTool.EMPTY_STR;

        return getStringFromBytes(serializer.serialize(graph), BinaryStringFormat.HexDecimal);
    }

    /**
     * 직렬화된 문자열을 역직렬화하여, 객체로 빌드합니다.
     *
     * @param serializer    the serializer
     * @param clazz         the clazz
     * @param serializedStr the serialized str
     * @return the 객체
     */
    public static <T> T deserializeFromString(final ISerializer serializer,
                                              final Class<T> clazz,
                                              final String serializedStr) {
        shouldNotBeNull(serializer, "serializer");
        if (StringTool.isEmpty(serializedStr)) {
            return null;
        }

        return serializer.deserialize(getBytesFromHexString(serializedStr), clazz);
    }

    /**
     * 객체를 직렬화하여 {@link java.io.OutputStream} 으로 변환합니다.
     *
     * @param serializer the serializer
     * @param graph      the graph
     * @return the output stream
     * @throws IOException the iO exception
     */
    public static OutputStream serializeAsStream(final ISerializer serializer, Object graph) throws IOException {
        shouldNotBeNull(serializer, "serializer");
        if (graph == null) {
            return new ByteArrayOutputStream();
        }

        return StreamTool.toOutputStream(serializer.serialize(graph));
    }

    /**
     * {@link java.io.InputStream} 을 읽어 역직렬화하여, 객체를 빌드합니다.  @param serializer the serializer
     *
     * @param clazz       the clazz
     * @param inputStream the input stream
     * @return the t
     * @throws IOException the iO exception
     */
    public static <T> T deserializeFromStream(ISerializer serializer,
                                              Class<T> clazz,
                                              InputStream inputStream) throws IOException {
        shouldNotBeNull(serializer, "serializer");
        if (inputStream == null)
            return null;

        return serializer.deserialize(StreamTool.toByteArray(inputStream), clazz);
    }

    /**
     * 지정된 객체를 직렬화하여, byte[] 로 변환합니다.
     *
     * @param graph 직렬화 대상 객체
     * @return 직렬화된 정보
     */
    public static byte[] serializeObject(Object graph) {
        return getBinarySerializer().serialize(graph);
    }

    /**
     * 직렬화된 byte[] 정보를 역직렬화하여, 객체로 변환합니다.
     *
     * @param bytes 직렬화된 정보
     * @param clazz the clazz
     * @return 역직렬화된 객체
     */
    public static <T> T deserializeObject(byte[] bytes, Class<T> clazz) {
        return getBinarySerializer().deserialize(bytes, clazz);
    }

    /**
     * 객체를 {@link BinarySerializer} 를 이용하여 deep copy 를 수행합니다.  @param graph the graph
     *
     * @return the t
     */
    @SuppressWarnings( "unchecked" )
    public static <T> T copyObject(T graph) {
        if (graph == null)
            return null;

        return (T) deserializeObject(serializeObject(graph), graph.getClass());
    }

    /**
     * 비동기 방식으로 객체를 직렬화합니다.
     *
     * @param graph 객체
     * @return 직렬화 결과
     */
    public static Future<byte[]> serializeObjectAsync(final Object graph) {
        if (graph == null) {
            return AsyncTool.getTaskHasResult(new byte[0]);
        }
        return AsyncTool.startNew(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return getBinarySerializer().serialize(graph);
            }
        });
    }

    /**
     * 직렬화한 정보를 역직렬화하여 객체로 변환합니다.
     *
     * @param bytes 직렬화된 정보
     * @param clazz 역직렬화할 객체의 수형
     * @return 역직렬화한 객체
     */
    public static <T> Future<T> deserializeObjectAsync(final byte[] bytes, final Class<T> clazz) {
        if (ArrayTool.isEmpty(bytes)) {
            return null;
        }
        return AsyncTool.startNew(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return getBinarySerializer().deserialize(bytes, clazz);
            }
        });
    }

    /**
     * 객체를 비동기 방식으로 복사합니다.
     *
     * @param graph 원본 인스턴스
     * @return 복사한 인스턴스
     */
    @SuppressWarnings( "unchecked" )
    public static <T> Future<T> copyObjectAsync(final T graph) {
        if (graph == null) {
            return AsyncTool.getTaskHasResult(graph);
        }

        return AsyncTool.startNew(new Callable<T>() {
            @Override
            public T call() throws Exception {
                byte[] bytes = getBinarySerializer().serialize(graph);
                return (T) getBinarySerializer().deserialize(bytes, graph.getClass());
            }
        });
    }
}
