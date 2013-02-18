package kr.debop4j.core.tools;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static kr.debop4j.core.Guard.shouldNotBeNull;


/**
 * {@link java.io.InputStream}, {@link java.io.OutputStream} 에 대한 Utility Class 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 13
 */
@Slf4j
public final class StreamTool {

    public static final int BUFFER_SIZE = 4096;

    private StreamTool() {
    }

    /**
     * {@link java.io.InputStream} 내용을 읽어, {@link java.io.OutputStream} 에 씁니다.
     *
     * @param inputStream  원본 스트림
     * @param outputStream 대상 스트림
     * @return 복사한 데이터 길이
     * @throws java.io.IOException
     */
    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        shouldNotBeNull(inputStream, "inputStream");
        shouldNotBeNull(outputStream, "outputStream");

        byte[] buffer = new byte[BUFFER_SIZE];
        int n;
        long size = 0;
        while ((n = inputStream.read(buffer, 0, BUFFER_SIZE)) > 0) {
            outputStream.write(buffer, 0, n);
            size += n;
        }
        //Streams.copy(inputStream, outputStream, buffer);
        return size;
    }

    /**
     * {@link java.io.InputStream} 내용을 읽어 바이트 배열로 반환합니다.
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        shouldNotBeNull(inputStream, "inputStream");

        @Cleanup
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        copy(inputStream, outputStream);

        return outputStream.toByteArray();
    }

    /**
     * 바이트 배열을 {@link java.io.OutputStream} 에 씁니다.
     *
     * @param bytes 입력 바이트 배열
     * @return 바이트 배열 정보가 쓰여진 {@link java.io.OutputStream}
     * @throws java.io.IOException
     */
    public static OutputStream toOutputStream(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0)
            return new ByteArrayOutputStream();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
        outputStream.write(bytes);

        return outputStream;
    }

    /**
     * {@link java.io.InputStream} 을 읽어 UTF-8 문자열로 변환합니다.
     *
     * @param inputStream 읽을 스트림
     * @return 읽은 UTF-8 문자열
     * @throws java.io.IOException
     */
    public static String toString(InputStream inputStream) throws IOException {
        byte[] bytes = toByteArray(inputStream);
        return StringTool.getUtf8String(bytes);
    }

    /**
     * UTF-8 문자열을 {@link java.io.OutputStream} 에 씁니다.
     *
     * @param str 변환할 UTF-8 문자열
     * @return 데이터를 복사한 {@link java.io.OutputStream}
     * @throws java.io.IOException
     */
    public static OutputStream toOutputStream(String str) throws IOException {
        if (StringTool.isEmpty(str))
            return new ByteArrayOutputStream();

        return toOutputStream(StringTool.getUtf8Bytes(str));
    }
}
