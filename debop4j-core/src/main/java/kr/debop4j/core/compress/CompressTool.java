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

package kr.debop4j.core.compress;

import kr.debop4j.core.BinaryStringFormat;
import kr.debop4j.core.parallelism.AsyncTool;
import kr.debop4j.core.tools.StreamTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static kr.debop4j.core.Guard.shouldNotBeNull;
import static kr.debop4j.core.tools.StreamTool.toByteArray;
import static kr.debop4j.core.tools.StringTool.*;


/**
 * 압축 툴
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 13
 */
public abstract class CompressTool {

    private static final Logger log = LoggerFactory.getLogger(CompressTool.class);

    private CompressTool() {}

    private static final byte[] buffer = new byte[ICompressor.BUFFER_SIZE];

    /**
     * Compress string.
     *
     * @param compressor the compressor
     * @param plainText the plain text
     * @return the string
     */
    public static String compressString(final ICompressor compressor,
                                        final String plainText) {
        return compressString(compressor,
                              plainText,
                              BinaryStringFormat.HexDecimal);
    }

    /**
     * Compress string.
     *
     * @param compressor the compressor
     * @param plainText the plain text
     * @param stringFormat the string format
     * @return the string
     */
    public static String compressString(final ICompressor compressor,
                                        final String plainText,
                                        final BinaryStringFormat stringFormat) {
        shouldNotBeNull(compressor, "compressor");
        if (isEmpty(plainText)) return "";

        if (log.isTraceEnabled())
            log.trace("다음 문자열을 압축합니다... plainText=[{}]", ellipsisChar(plainText, 80));

        byte[] compressedBytes = compressor.compress(getUtf8Bytes(plainText));
        return getStringFromBytes(compressedBytes, stringFormat);
    }

    /**
     * Compress string async.
     *
     * @param compressor the compressor
     * @param plainText the plain text
     * @return the future
     */
    public static Future<String> compressStringAsync(final ICompressor compressor,
                                                     final String plainText) {
        return compressStringAsync(compressor,
                                   plainText,
                                   BinaryStringFormat.HexDecimal);
    }

    /**
     * Compress string async.
     *
     * @param compressor the compressor
     * @param plainText the plain text
     * @param stringFormat the string format
     * @return the future
     */
    public static Future<String> compressStringAsync(final ICompressor compressor,
                                                     final String plainText,
                                                     final BinaryStringFormat stringFormat) {
        shouldNotBeNull(compressor, "compressor");
        if (isEmpty(plainText)) {
            AsyncTool.getTaskHasResult("");
        }

        if (log.isTraceEnabled())
            log.trace("다음 문자열을 압축합니다... plainText=[{}]", ellipsisChar(plainText, 80));

        return AsyncTool.startNew(new Callable<String>() {
            @Override
            public String call() throws Exception {
                byte[] compressedBytes = compressor.compress(getUtf8Bytes(plainText));
                return getStringFromBytes(compressedBytes, stringFormat);
            }
        });
    }

    /**
     * Decompress string.
     *
     * @param compressor the compressor
     * @param compressedText the compressed text
     * @return the string
     */
    public static String decompressString(final ICompressor compressor,
                                          final String compressedText) {
        return decompressString(compressor,
                                compressedText,
                                BinaryStringFormat.HexDecimal);
    }

    /**
     * Decompress string.
     *
     * @param compressor the compressor
     * @param compressedText the compressed text
     * @param stringFormat the string format
     * @return the string
     */
    public static String decompressString(final ICompressor compressor,
                                          final String compressedText,
                                          final BinaryStringFormat stringFormat) {
        shouldNotBeNull(compressor, "compressor");
        if (isEmpty(compressedText)) return "";

        if (log.isTraceEnabled())
            log.trace("압축된 문자열을 복원합니다... compressedText=[{}]", ellipsisChar(compressedText, 80));

        byte[] plainBytes = compressor.decompress(getBytesFromString(compressedText, stringFormat));

        String plainText = getUtf8String(plainBytes);

        if (log.isTraceEnabled())
            log.trace("압축 복원한 문자열입니다... plainText=[{}]", ellipsisChar(plainText, 80));

        return plainText;
    }

    /**
     * Decompress string async.
     *
     * @param compressor the compressor
     * @param compressedText the compressed text
     * @return the future
     */
    public static Future<String> decompressStringAsync(final ICompressor compressor,
                                                       final String compressedText) {
        return decompressStringAsync(compressor,
                                     compressedText,
                                     BinaryStringFormat.HexDecimal);
    }

    /**
     * Decompress string async.
     *
     * @param compressor the compressor
     * @param compressedText the compressed text
     * @param stringFormat the string format
     * @return the future
     */
    public static Future<String> decompressStringAsync(final ICompressor compressor,
                                                       final String compressedText,
                                                       final BinaryStringFormat stringFormat) {
        shouldNotBeNull(compressor, "compressor");

        if (isEmpty(compressedText)) {
            return AsyncTool.getTaskHasResult("");
        }

        if (log.isTraceEnabled())
            log.trace("압축된 문자열을 복원합니다... compressedText=[{}]", ellipsisChar(compressedText, 80));

        return AsyncTool.startNew(new Callable<String>() {
            @Override
            public String call() throws Exception {
                byte[] plainBytes = compressor.decompress(getBytesFromString(compressedText,
                                                                             stringFormat));
                String plainText = getUtf8String(plainBytes);

                if (log.isTraceEnabled())
                    log.trace("압축 복원한 문자열입니다... plainText=[{}]", ellipsisChar(plainText, 80));

                return plainText;
            }
        });
    }

    /**
     * Compress stream.
     *
     * @param compressor the compressor
     * @param inputStream the input stream
     * @return the output stream
     * @throws IOException the iO exception
     */
    public static OutputStream compressStream(final ICompressor compressor,
                                              final InputStream inputStream) throws IOException {
        shouldNotBeNull(compressor, "compressor");
        shouldNotBeNull(inputStream, "inputStream");

        byte[] plainBytes = toByteArray(inputStream);
        byte[] compressedBytes = compressor.compress(plainBytes);

        return StreamTool.toOutputStream(compressedBytes);
    }

    /**
     * Decompress stream.
     *
     * @param compressor the compressor
     * @param inputStream the input stream
     * @return the output stream
     * @throws IOException the iO exception
     */
    public static OutputStream decompressStream(final ICompressor compressor,
                                                final InputStream inputStream) throws IOException {
        shouldNotBeNull(compressor, "compressor");
        shouldNotBeNull(inputStream, "inputStream");

        byte[] compressedBytes = toByteArray(inputStream);
        byte[] plainBytes = compressor.decompress(compressedBytes);

        return StreamTool.toOutputStream(plainBytes);
    }

    /**
     * Compress stream async.
     *
     * @param compressor the compressor
     * @param inputStream the input stream
     * @return the future
     */
    public static Future<OutputStream> compressStreamAsync(final ICompressor compressor,
                                                           final InputStream inputStream) {
        shouldNotBeNull(compressor, "compressor");
        shouldNotBeNull(inputStream, "inputStream");

        return AsyncTool.startNew(new Callable<OutputStream>() {
            @Override
            public OutputStream call() throws Exception {
                return compressStream(compressor, inputStream);
            }
        });
    }

    /**
     * Decompress stream async.
     *
     * @param compressor the compressor
     * @param inputStream the input stream
     * @return the future
     */
    public static Future<OutputStream> decompressStreamAsync(final ICompressor compressor,
                                                             final InputStream inputStream) {
        shouldNotBeNull(compressor, "compressor");
        shouldNotBeNull(inputStream, "inputStream");

        return AsyncTool.startNew(new Callable<OutputStream>() {
            @Override
            public OutputStream call() throws Exception {
                byte[] compressedBytes = toByteArray(inputStream);
                byte[] plainBytes = compressor.decompress(compressedBytes);

                return StreamTool.toOutputStream(plainBytes);
            }
        });
    }
}
