package kr.debop4j.core.io;

import com.google.common.collect.Lists;
import kr.debop4j.core.tools.StringTool;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.Assert.*;

/**
 * kr.debop4j.core.io.FileToolTest
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 11.
 */
public class FileToolTest {


    public static final String TEST_TEXT = "동해물과 백두산이 마르고 닳도록, 하느님이 보우하사 우리나라 만세!!! Hello World. 안녕 세계여";

    @Test
    public void createAndDelete() throws Exception {
        Path path = Paths.get("test.txt");

        try {
            FileTool.createFile(path);
            assertTrue(FileTool.exists(path));

            FileTool.delete(path);
            assertFalse(FileTool.exists(path));
        } finally {
            FileTool.deleteIfExists(path);
        }
    }

    @Test
    public void binaryReadAndWrite() throws Exception {
        Path path = Paths.get("test.dat");
        byte[] bytes = StringTool.getUtf8Bytes(TEST_TEXT);

        try {
            FileTool.createFile(path);
            FileTool.write(path, bytes, StandardOpenOption.WRITE);

            byte[] readBytes = FileTool.readAllBytes(path);
            assertArrayEquals(bytes, readBytes);
        } finally {
            FileTool.deleteIfExists(path);
        }
    }

    @Test
    public void charReadAndWrite() throws Exception {
        Path path = Paths.get("test.txt");
        int lineCount = 100;
        List<String> lines = Lists.newArrayListWithCapacity(lineCount);

        for (int i = 0; i < lineCount; i++)
            lines.add(TEST_TEXT);

        try {
            FileTool.createFile(path);
            FileTool.write(path, lines, StringTool.UTF8, StandardOpenOption.WRITE);

            List<String> readLines = FileTool.readAllLines(path);
            assertEquals(lines.size(), readLines.size());
        } finally {
            FileTool.deleteIfExists(path);
        }
    }
}
