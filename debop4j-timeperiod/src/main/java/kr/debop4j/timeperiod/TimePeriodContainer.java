package kr.debop4j.timeperiod;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * kr.debop4j.timeperiod.TimePeriodContainer
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오전 2:20
 */
@Slf4j
public class TimePeriodContainer implements ITimePeriodContainer {

    private static final long serialVersionUID = -7112720659283751048L;

    @Override
    public void setStart(DateTime start) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setEnd(DateTime end) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime getStart() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime getEnd() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Duration getDuration() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDurationDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasStart() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasEnd() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasPeriod() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isMoment() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isAnytime() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isReadonly() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setup(DateTime newStart, DateTime newEnd) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriod copy(Duration offset) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void move(Duration offset) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isSamePeriod(ITimePeriod other) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasInside(DateTime moment) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasInside(ITimePeriod other) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean intersectsWith(ITimePeriod other) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean overlapsWith(ITimePeriod other) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PeriodRelation getRelation(ITimePeriod other) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDescription(DateTimeFormatter formatter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriod getIntersection(ITimePeriod other) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriod getUnion(ITimePeriod other) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean containsPeriod(ITimePeriod target) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addAll(Iterable<ITimePeriod> periods) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sortByStart(OrderDirection sortDir) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sortByEnd(OrderDirection sortDir) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sortByDuration(OrderDirection sortDir) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int compareTo(ITimePeriod o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int size() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isEmpty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean contains(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterator<ITimePeriod> iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object[] toArray() {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean add(ITimePeriod iTimePeriod) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean remove(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean addAll(Collection<? extends ITimePeriod> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean addAll(int index, Collection<? extends ITimePeriod> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clear() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriod get(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriod set(int index, ITimePeriod element) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void add(int index, ITimePeriod element) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriod remove(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int indexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListIterator<ITimePeriod> listIterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListIterator<ITimePeriod> listIterator(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ITimePeriod> subList(int fromIndex, int toIndex) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
