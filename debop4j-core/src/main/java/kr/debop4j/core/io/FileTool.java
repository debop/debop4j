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

import kr.debop4j.core.parallelism.AsyncTool;
import kr.debop4j.core.tools.StringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static kr.debop4j.core.tools.StringTool.listToString;

/**
 * 파일 관련 Tool
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 11
 */
public class FileTool {

    private static final Logger log = LoggerFactory.getLogger(FileTool.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    /** The constant DEFAULT_BUFFER_SIZE. */
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    /** The constant UTF8. */
    public static final Charset UTF8 = Charset.forName("UTF-8");

    private FileTool() { }

    /**
     * Combine path.
     *
     * @param base  the base
     * @param other the other
     * @return the path
     */
    public static Path combine(String base, String... other) {
        return Paths.get(base, other);
    }

    /**
     * Combine path.
     *
     * @param base   the base
     * @param others the others
     * @return the path
     */
    public static Path combine(Path base, String... others) {
        Path result = base;

        for (String other : others)
            result = result.resolve(other);

        return result;
    }

    /**
     * Combine path.
     *
     * @param base  the base
     * @param other the other
     * @return the path
     */
    public static Path combine(Path base, Path other) {
        return base.resolve(other);
    }

    /**
     * Create directory.
     *
     * @param dir   the dir
     * @param attrs the attrs
     * @return the path
     * @throws IOException the iO exception
     */
    public static Path createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        if (isDebugEnabled)
            log.debug("디렉토리를 생성합니다. dir=[{}]", dir);
        return Files.createDirectory(dir, attrs);
    }

    /**
     * Create directories.
     *
     * @param dir   the dir
     * @param attrs the attrs
     * @return the path
     * @throws IOException the iO exception
     */
    public static Path createDirectories(Path dir, FileAttribute<?>... attrs) throws IOException {
        return Files.createDirectories(dir, attrs);
    }

    /**
     * Create file.
     *
     * @param path  the path
     * @param attrs the attrs
     * @return the path
     * @throws IOException the iO exception
     */
    public static Path createFile(Path path, FileAttribute<?>... attrs) throws IOException {
        if (isDebugEnabled)
            log.debug("파일 생성. path=[{}], attrs=[{}]", path, listToString(attrs));
        return Files.createFile(path, attrs);
    }

    /**
     * Copy void.
     *
     * @param source the source
     * @param target the target
     * @throws IOException the iO exception
     */
    public static void copy(Path source, Path target) throws IOException {
        Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Copy void.
     *
     * @param source  the source
     * @param target  the target
     * @param options the options
     * @throws IOException the iO exception
     */
    public static void copy(Path source, Path target, CopyOption... options) throws IOException {
        Files.copy(source, target, options);
    }

    /**
     * Copy async.
     *
     * @param source  the source
     * @param target  the target
     * @param options the options
     * @return the future
     */
    public static Future<Void> copyAsync(final Path source, final Path target, final CopyOption... options) {
        return
                AsyncTool.startNew(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        copy(source, target, options);
                        return null;
                    }
                });
    }

    /**
     * 파일을 이동합니다.  @param src the src
     *
     * @param dst the dst
     * @throws IOException the iO exception
     */
    public static void move(Path src, Path dst) throws IOException {
        if (isTraceEnabled)
            log.trace("파일을 이동합니다. src=[{}], dst=[{}]", src, dst);
        Files.move(src,
                dst,
                StandardCopyOption.ATOMIC_MOVE,
                StandardCopyOption.COPY_ATTRIBUTES,
                StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Move void.
     *
     * @param src     the src
     * @param dst     the dst
     * @param options the options
     * @throws IOException the iO exception
     */
    public static void move(Path src, Path dst, StandardCopyOption... options) throws IOException {
        if (isTraceEnabled)
            log.trace("파일을 이동합니다. src=[{}], dst=[{}], options=[{}]", src, dst, StringTool.listToString(options));
        Files.move(src, dst, options);
    }

    /**
     * Move async.
     *
     * @param src     the src
     * @param dst     the dst
     * @param options the options
     * @return the future
     */
    public static Future<Void> moveAsync(final Path src, final Path dst, final StandardCopyOption... options) {
        if (isTraceEnabled)
            log.trace("비동기 방식으로 파일을 이동합니다. src=[{}], dst=[{}], options=[{}]", src, dst, StringTool.listToString(options));

        return AsyncTool.startNew(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                move(src, dst, options);
                return null;
            }
        });
    }


    /**
     * Delete void.
     *
     * @param path the path
     * @throws IOException the iO exception
     */
    public static void delete(Path path) throws IOException {
        if (isDebugEnabled)
            log.debug("디렉토리/파일 삭제. path=[{}]", path);
        Files.delete(path);
    }

    /**
     * Delete if exists.
     *
     * @param path the path
     * @throws IOException the iO exception
     */
    public static void deleteIfExists(Path path) throws IOException {
        if (exists(path))
            Files.deleteIfExists(path);
    }

    /**
     * Delete directory.
     *
     * @param directory the directory
     * @param deep      the deep
     * @throws IOException the iO exception
     */
    public static void deleteDirectory(Path directory, boolean deep) throws IOException {
        if (isDebugEnabled)
            log.debug("directory=[{}] 를 삭제합니다. deep=[{}]", directory, deep);

        if (!deep) {
            deleteIfExists(directory);
        } else {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (isTraceEnabled) log.trace("Directory 삭제 dir=[{}]", dir);
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (isTraceEnabled) log.trace("파일 삭제 file=[{}]", file);
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    /**
     * Delete directory async.
     *
     * @param directory the directory
     * @param deep      the deep
     * @return the future
     */
    public static Future<Void> deleteDirectoryAsync(final Path directory, final boolean deep) {
        if (isTraceEnabled)
            log.trace("Directory를 삭제합니다. directory=[{}], deep=[{}]", directory, deep);

        return AsyncTool.startNew(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                deleteDirectory(directory, deep);
                return null;
            }
        });
    }

    /**
     * Exists boolean.
     *
     * @param path        the path
     * @param linkOptions the link options
     * @return the boolean
     */
    public static boolean exists(Path path, LinkOption... linkOptions) {
        if (isTraceEnabled)
            log.trace("파일 존재를 확인합니다. path=[{}], linkOptions=[{}]", path, listToString(linkOptions));
        return Files.exists(path, linkOptions);
    }

    /**
     * Read all bytes.
     *
     * @param path the path
     * @return the byte [ ]
     * @throws IOException the iO exception
     */
    public static byte[] readAllBytes(Path path) throws IOException {
        if (isTraceEnabled)
            log.trace("파일로부터 모든 내용을 읽어옵니다. path=[{}]", path);
        return Files.readAllBytes(path);
    }

    /**
     * Read all lines.
     *
     * @param bytes   the bytes
     * @param charset the charset
     * @return the list
     */
    public static List<String> readAllLines(byte[] bytes, Charset charset) {
        List<String> lines = new ArrayList<String>();

        if (bytes == null || bytes.length == 0)
            return lines;

        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
             Reader in = new InputStreamReader(is, charset);
             BufferedReader br = new BufferedReader(in)) {

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

    /**
     * Read all bytes async.
     *
     * @param path        the path
     * @param openOptions the open options
     * @return the future
     */
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

    /**
     * Read all lines.
     *
     * @param path the path
     * @return the list
     * @throws IOException the iO exception
     */
    public static List<String> readAllLines(Path path) throws IOException {
        return readAllLines(path, StringTool.UTF8);
    }

    /**
     * Read all lines.
     *
     * @param path the path
     * @param cs   the cs
     * @return the list
     * @throws IOException the iO exception
     */
    public static List<String> readAllLines(Path path, Charset cs) throws IOException {
        if (isTraceEnabled)
            log.trace("파일 내용을 문자열로 읽어드립니다. path=[{}], charset=[{}]", path, cs);
        return Files.readAllLines(path, cs);
    }

    /**
     * Read all lines async.
     *
     * @param path the path
     * @return the future
     */
    public static Future<List<String>> readAllLinesAsync(final Path path) {
        return readAllLinesAsync(path, UTF8, StandardOpenOption.READ);
    }

    /**
     * Read all lines async.
     *
     * @param path        the path
     * @param openOptions the open options
     * @return the future
     */
    public static Future<List<String>> readAllLinesAsync(final Path path, final OpenOption... openOptions) {
        return readAllLinesAsync(path, UTF8, openOptions);
    }

    /**
     * Read all lines async.
     *
     * @param path        the path
     * @param cs          the cs
     * @param openOptions the open options
     * @return the future
     */
    public static Future<List<String>> readAllLinesAsync(final Path path,
                                                         final Charset cs,
                                                         final OpenOption... openOptions) {
        if (isTraceEnabled)
            log.trace("파일 내용을 문자열로 읽어드립니다. path=[{}], charset=[{}], openOption=[{}]", path, cs, listToString(openOptions));

        return AsyncTool.startNew(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                Future<byte[]> readTask = readAllBytesAsync(path, openOptions);
                return readAllLines(readTask.get(), cs);
            }
        });
    }

    /**
     * Write void.
     *
     * @param target      the target
     * @param bytes       the bytes
     * @param openOptions the open options
     * @throws IOException the iO exception
     */
    public static void write(Path target, byte[] bytes, OpenOption... openOptions) throws IOException {
        if (isTraceEnabled)
            log.trace("파일에 binary 형태의 정보를 씁니다. target=[{}], openOptions=[{}]", target, listToString(openOptions));

        Files.write(target, bytes, openOptions);
    }

    /**
     * Write void.
     *
     * @param target      the target
     * @param lines       the lines
     * @param cs          the cs
     * @param openOptions the open options
     * @throws IOException the iO exception
     */
    public static void write(Path target,
                             Iterable<String> lines,
                             Charset cs,
                             OpenOption... openOptions) throws IOException {
        if (isTraceEnabled)
            log.trace("파일에 텍스트 정보를 씁니다. target=[{}], lines=[{}], charset=[{}], openOptions=[{}]",
                    target, listToString(lines), cs, listToString(openOptions));
        Files.write(target, lines, cs, openOptions);
    }


    /**
     * Write async.
     *
     * @param target      the target
     * @param bytes       the bytes
     * @param openOptions the open options
     * @return the future
     */
    public static Future<Void> writeAsync(final Path target,
                                          final byte[] bytes,
                                          final OpenOption... openOptions) {
        if (isTraceEnabled)
            log.trace("비동기 방식으로 데이터를 파일에 씁니다. target=[{}], openOptions=[{}]", target, listToString(openOptions));

        return AsyncTool.startNew(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(target, openOptions)) {
                    Future<Integer> result = fileChannel.write(ByteBuffer.wrap(bytes), 0);
                    while (!result.isDone()) {
                        Thread.sleep(1);
                    }
                    return null;
                } catch (Exception e) {
                    log.error("비동기 방식으로 파일에 쓰는 동안 예외가 발생했습니다.", e);
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Write async.
     *
     * @param target      the target
     * @param lines       the lines
     * @param cs          the cs
     * @param openOptions the open options
     * @return the future
     */
    public static Future<Void> writeAsync(final Path target,
                                          final Iterable<String> lines,
                                          final Charset cs,
                                          final OpenOption... openOptions) {
        String allText = StringTool.join(lines, System.lineSeparator());
        return writeAsync(target, cs.encode(allText).array(), openOptions);
    }
}
