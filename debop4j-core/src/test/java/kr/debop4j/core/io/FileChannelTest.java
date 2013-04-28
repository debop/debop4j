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

import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * kr.debop4j.core.io.FileChannelTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 4. 28. 오후 9:29
 */
@Slf4j
public class FileChannelTest {

    public static final String TEST_TEXT = "동해물과 백두산이 마르고 닳도록, 하느님이 보우하사 우리나라 만세!!! Hello World. 안녕 세계여 \n";
    public static final Charset UTF8 = Charset.forName("UTF-8");

    @Test
    public void bufferedStreams() throws Exception {
        Path path = Paths.get("channel.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(path, UTF8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (int i = 0; i < 100; i++) {
                writer.write(TEST_TEXT);
            }
            writer.flush();
        }

        List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
        for (String line : lines)
            System.out.println(line);

        Files.deleteIfExists(path);
    }

    @Test
    public void asynchronousReadWrite() throws Exception {
        Path path = Paths.get("async.txt");

        // Write
        AsynchronousFileChannel fc = null;
        try {
            fc = AsynchronousFileChannel.open(path,
                                              StandardOpenOption.CREATE,
                                              StandardOpenOption.WRITE);

            ByteBuffer buffer = ByteBuffer.wrap(StringTool.replicate(TEST_TEXT, 1000).getBytes(UTF8));
            Future<Integer> result = fc.write(buffer, 0);
            while (!result.isDone()) {
                System.out.println("Do something else while writing...");
            }
            System.out.println("Write done: " + result.isDone());
            System.out.println("Bytes write: " + result.get());
        } finally {
            assert fc != null;
            fc.close();
        }

        // Read
        fc = null;
        try {
            fc = AsynchronousFileChannel.open(path,
                                              StandardOpenOption.READ,
                                              StandardOpenOption.DELETE_ON_CLOSE);

            ByteBuffer buffer = ByteBuffer.allocate((int) fc.size());

            Future<Integer> result = fc.read(buffer, 0);
            while (!result.isDone()) {
                System.out.println("Do something else while reading...");
            }
            System.out.println("Read done: " + result.isDone());
            System.out.println("Bytes read: " + result.get());

            buffer.flip();
            byte[] bytes = buffer.array();

            List<String> lines = readAllLines(bytes, UTF8);
            for (String line : lines) {
                System.out.println(line);
            }
            // System.out.print(UTF8.decode(buffer));
            buffer.clear();
        } finally {
            assert fc != null;
            fc.close();
        }
    }

    public List<String> readAllLines(byte[] bytes, Charset charset) {
        if (bytes == null || bytes.length == 0)
            return new ArrayList<String>();

        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
             Reader in = new InputStreamReader(is, charset);
             BufferedReader br = new BufferedReader(in)) {

            List<String> lines = new ArrayList<String>();
            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                lines.add(line);
            }
            return lines;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
