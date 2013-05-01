package kr.debop4j.core.guava;

import com.google.common.collect.Iterables;
import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.collection.NumberRange;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * kr.debop4j.core.guava.IterablesTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 11.
 */
@Slf4j
public class IterablesTest extends AbstractTest {

    @Test
    public void testIterablesPartition() {

        int[] limits = new int[] { 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108 };
        int[] partitions = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };

        for (int limit : limits) {
            for (int partition : partitions) {
                int partitionSize = limit / partition + ((limit % partition) > 0 ? 1 : 0);

                Iterable<List<Integer>> parts = Iterables.partition(NumberRange.range(0, limit), partitionSize);

                log.debug(Iterables.toString(parts));
            }
        }
    }
}
