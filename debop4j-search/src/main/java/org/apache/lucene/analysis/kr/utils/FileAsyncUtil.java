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

package org.apache.lucene.analysis.kr.utils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import kr.debop4j.core.parallelism.AsyncTool;
import kr.debop4j.core.tools.StringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static kr.debop4j.core.tools.StringTool.listToString;

/**
 * org.apache.lucene.analysis.kr.utils.FileAsyncUtil
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 4. 28. 오후 11:50
 */
public class FileAsyncUtil {

    private static final Logger log = LoggerFactory.getLogger(FileAsyncUtil.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    public static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final Charset UTF8 = Charset.forName("UTF-8");

    private FileAsyncUtil() {}

    public static Future<List<String>> readAllLinesAsync(final Path path) {
        return readAllLinesAsync(path, UTF8, StandardOpenOption.READ);
    }

    public static Future<List<String>> readAllLinesAsync(final Path path, final OpenOption... openOptions) {
        return readAllLinesAsync(path, UTF8, openOptions);
    }

    public static Future<List<String>> readAllLinesAsync(final Path path,
                                                         final Charset cs,
                                                         final OpenOption... openOptions) {
        if (isTraceEnabled)
            log.trace("파일 내용을 문자열로 읽어드립니다. path=[{}], charset=[{}], openOption=[{}]", path, cs, listToString(openOptions));

        return AsyncTool.startNew(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                Future<byte[]> readTask = readAllBytesAsync(path, openOptions);

                return Lists.newArrayList(Splitter.on(System.lineSeparator())
                                                  .trimResults(CharMatcher.WHITESPACE)
                                                  .split(new String(readTask.get(), cs)));
            }
        });
    }

    public static Future<byte[]> readAllBytesAsync(final Path path, final OpenOption... openOptions) {
        assert path != null;

        if (isTraceEnabled)
            log.trace("비동기 방식으로 파일 정보를 읽어 byte array로 반환합니다. file=[{}], openOptions=[{}]",
                      path, StringTool.listToString(openOptions));

        return AsyncTool.startNew(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, openOptions)) {
                    ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
                    Future<Integer> result = fileChannel.read(buffer, 0);
                    while (!result.isDone()) {
                        Thread.sleep(1);
                    }
                    buffer.flip();
                    return buffer.array();
                } catch (Exception e) {
                    log.error("파일 내용을 읽어오는데 실패했습니다.", e);
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
