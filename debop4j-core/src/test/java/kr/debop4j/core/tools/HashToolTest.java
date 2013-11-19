package kr.debop4j.core.tools;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import kr.debop4j.core.YearWeek;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@Slf4j
public class HashToolTest {

    /**
     * 테스트에 성공합니다. 아마 Scala IDE나 컴파일러의 문제로 인해, 에러로 뜨는 것 같습니다.
     */
    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void computeHashTest() {
        int a = HashTool.compute(1, 2);
        int b = HashTool.compute(2, 1);

        assertNotEquals(a, b);
        assertEquals(a, HashTool.compute(1, 2));

        int withNull1 = HashTool.compute(new YearWeek(2013, 1), null);
        int withNull2 = HashTool.compute(null, new YearWeek(2013, 1));
        int withNull3 = HashTool.compute(new YearWeek(2013, 1), null);

        assertNotEquals(withNull1, withNull2);
        assertNotEquals(withNull2, withNull3);
        assertEquals(withNull1, withNull3);
    }
}
