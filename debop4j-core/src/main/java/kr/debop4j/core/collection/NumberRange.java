package kr.debop4j.core.collection;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;

/**
 * 특정 범위에 해당하는 숫자들을 열거하도록 합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 11
 */
@Slf4j
public abstract class NumberRange<T extends Number> implements Iterable<T> {

    private NumberRange() {}

    public static IntRange range(int fromInclude, int toExclude, int step) {
        return new IntRange(fromInclude, toExclude, step);
    }

    public static IntRange range(int fromInclude, int toExclude) {
        int step = fromInclude <= toExclude ? 1 : -1;
        return range(fromInclude, toExclude, step);
    }

    public static IntRange range(int count) {
        return range(0, count);
    }

    public static LongRange range(long fromInclude, long toExclude, long step) {
        return new LongRange(fromInclude, toExclude, step);
    }

    public static LongRange range(long fromInclude, long toExclude) {
        long step = fromInclude <= toExclude ? 1L : -1L;
        return range(fromInclude, toExclude, step);
    }

    public static LongRange range(long count) {
        return range(0L, count);
    }

    public static List<IntRange> partition(IntRange range, int partitionCount) {
        int rangeSize = range.size();
        int stepSign = range.getStep();
        int step = range.getStep();
        int partitionSize = rangeSize / partitionCount;
        int remainder = rangeSize % partitionCount;

        List<IntRange> partitions = Lists.newLinkedList();

        int fromInclude = range.fromInclude;
        for (int i = 0; i < partitionCount; i++) {
            int toExclude = fromInclude + (partitionSize + ((remainder > 0) ? 1 : 0)) * stepSign;
            if (remainder > 0)
                remainder--;
            toExclude = (step > 0)
                    ? Math.min(toExclude, range.getToExclude())
                    : Math.max(toExclude, range.getToExclude());

            IntRange partition = range(fromInclude, toExclude, step);
            partitions.add(partition);
            if (NumberRange.log.isDebugEnabled())
                NumberRange.log.debug("Partition 추가 = [{}]", partition);
            fromInclude = toExclude;
        }
        return partitions;
    }

    public static List<IntRange> partition(int fromInclude, int toExclude, int step, int partitionCount) {
        return partition(range(fromInclude, toExclude, step), partitionCount);
    }

    public static List<IntRange> partition(int fromInclude, int toExclude, int partitionCount) {
        return partition(range(fromInclude, toExclude), partitionCount);
    }

    public static List<IntRange> partition(int count, int partitionCount) {
        return partition(range(count), partitionCount);
    }

    public static List<LongRange> partition(LongRange range, int partitionCount) {
        long rangeSize = range.size();
        long stepSign = range.getStep();
        long step = range.getStep();
        long partitionSize = rangeSize / partitionCount;
        long remainder = rangeSize % partitionCount;

        List<LongRange> partitions = Lists.newLinkedList();

        long fromInclude = range.fromInclude;
        for (int i = 0; i < partitionCount; i++) {
            long toExclude = fromInclude + (partitionSize + ((remainder > 0) ? 1 : 0)) * stepSign;
            if (remainder > 0)
                remainder--;
            toExclude = (step > 0)
                    ? Math.min(toExclude, range.getToExclude())
                    : Math.max(toExclude, range.getToExclude());

            LongRange partition = range(fromInclude, toExclude, step);
            partitions.add(partition);
            if (NumberRange.log.isDebugEnabled())
                NumberRange.log.debug("Partition 추가 = [{}]", partition);
            fromInclude = toExclude;
        }
        return partitions;
    }

    public static List<LongRange> partition(long fromInclude, long toExclude, int step, int partitionCount) {
        return partition(range(fromInclude, toExclude, step), partitionCount);
    }

    public static List<LongRange> partition(long fromInclude, long toExclude, int partitionCount) {
        return partition(range(fromInclude, toExclude), partitionCount);
    }

    public static List<LongRange> partition(long count, int partitionCount) {
        return partition(range(count), partitionCount);
    }

    public static class IntRange extends NumberRange<Integer> implements Iterator<Integer> {

        @Getter
        int fromInclude, toExclude, step;
        @Getter
        int current;

        public IntRange(int fromInclude, int toExclude, int step) {
            this.fromInclude = fromInclude;
            this.toExclude = toExclude;
            this.step = step;
            this.current = this.fromInclude;
        }

        public int size() {
            return (toExclude - fromInclude) / step;
        }

        public int getStepSign() {
            return (step > 0) ? 1 : -1;
        }

        @Override
        public boolean hasNext() {
            return step > 0 ? current < toExclude : current > toExclude;
        }

        public Integer next() {
            int result = this.current;
            this.current += this.step;
            return result;
        }

        public void reset() {
            this.current = this.fromInclude;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<Integer> iterator() {
            return this;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("fromInclude", fromInclude)
                    .add("toExclude", toExclude)
                    .add("step", step)
                    .add("size", size())
                    .add("current", current)
                    .toString();
        }
    }

    public static class LongRange extends NumberRange<Long> implements Iterator<Long> {

        @Getter
        long fromInclude, toExclude, step;
        @Getter
        long current;

        public LongRange(long fromInclude, long toExclude, long step) {
            this.fromInclude = fromInclude;
            this.toExclude = toExclude;
            this.step = step;
            this.current = this.fromInclude;
        }

        public Long size() {
            return (toExclude - fromInclude) / step;
        }

        public Long getStepSign() {
            return (step > 0) ? 1L : -1L;
        }

        @Override
        public boolean hasNext() {
            return step > 0 ? current < toExclude : current > toExclude;
        }

        public Long next() {
            long result = this.current;
            this.current += this.step;
            return result;
        }

        public void reset() {
            this.current = this.fromInclude;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<Long> iterator() {
            return this;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("fromInclude", fromInclude)
                    .add("toExclude", toExclude)
                    .add("step", step)
                    .add("size", size())
                    .add("current", current)
                    .toString();
        }
    }
}
