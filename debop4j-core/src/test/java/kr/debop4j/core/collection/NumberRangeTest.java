package kr.debop4j.core.collection;

import com.google.common.collect.Lists;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * kr.debop4j.core.collection.NumberRangeTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 1. 12.
 */
@Slf4j
public class NumberRangeTest {

    @Test
    public void createIntRange() {
        NumberRange.IntRange intRange = NumberRange.range(10);
        assertEquals(0, intRange.getFromInclude());
        assertEquals(10, intRange.getToExclude());
        assertEquals(1, intRange.getStep());
        assertEquals(10, intRange.size());

        log.debug(StringTool.join(intRange, ","));
        intRange.reset();
        for (int x : intRange)
            System.out.print(x + ", ");
    }

    @Test
    public void createIntPartition() {
        int from = 0;
        int to = 100;
        int partitionCount = 4;
        int partitionSize = (to - from) / partitionCount + ((to - from) % partitionCount > 0 ? 1 : 0);

        List<NumberRange.IntRange> ranges = NumberRange.partition(from, to, partitionCount);

        assertEquals(4, ranges.size());

        for (int i = 0; i < partitionCount; i++) {
            NumberRange.IntRange intRange = ranges.get(i);

            assertEquals(from + i * partitionSize, intRange.getFromInclude());
            assertEquals(from + (i + 1) * partitionSize, intRange.getToExclude());
            assertEquals(1, intRange.getStep());
            assertEquals(partitionSize, intRange.size());
        }
    }

    @Test
    public void createIntPartitionUnnormal() {
        int from = 0;
        int to = 102;
        int partitionCount = 4;
        int partitionSize = (to - from) / partitionCount;

        List<NumberRange.IntRange> ranges = NumberRange.partition(from, to, partitionCount);
        assertEquals(4, ranges.size());

        List<int[]> expectedList =
                Lists.newArrayList(
                        new int[]{ 0, 26, 1, 26 },
                        new int[]{ 26, 52, 1, 26 },
                        new int[]{ 52, 77, 1, 25 },
                        new int[]{ 77, 102, 1, 25 });

        for (int i = 0; i < partitionCount; i++) {
            NumberRange.IntRange intRange = ranges.get(i);
            int[] expected = expectedList.get(i);
            if (log.isDebugEnabled())
                log.debug("NumberRange({})=[{}]", i, intRange);

            assertEquals(expected[0], intRange.getFromInclude());
            assertEquals(expected[1], intRange.getToExclude());
            assertEquals(expected[2], intRange.getStep());
            assertEquals(expected[3], intRange.size());
        }
    }

    @Test
    public void createIntPartitionUnnormalInverse() {
        int from = 102;
        int to = 0;
        int partitionCount = 4;
        int partitionSize = (to - from) / partitionCount;

        List<NumberRange.IntRange> ranges = NumberRange.partition(from, to, partitionCount);
        assertEquals(4, ranges.size());

        List<int[]> expectedList =
                Lists.newArrayList(
                        new int[]{ 102, 76, -1, 26 },
                        new int[]{ 76, 50, -1, 26 },
                        new int[]{ 50, 25, -1, 25 },
                        new int[]{ 25, 0, -1, 25 });

        for (int i = 0; i < partitionCount; i++) {
            NumberRange.IntRange intRange = ranges.get(i);
            int[] expected = expectedList.get(i);
            if (log.isDebugEnabled())
                log.debug("NumberRange({})=[{}]", i, intRange);

            assertEquals(expected[0], intRange.getFromInclude());
            assertEquals(expected[1], intRange.getToExclude());
            assertEquals(expected[2], intRange.getStep());
            assertEquals(expected[3], intRange.size());
        }
    }
}
