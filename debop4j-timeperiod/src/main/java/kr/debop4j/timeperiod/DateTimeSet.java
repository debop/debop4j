package kr.debop4j.timeperiod;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.List;

/**
 * {@link DateTime}을 요소로 가지는 컬렉션입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 7:02
 */
public interface DateTimeSet extends List<DateTime> {


    /// 최소값, 요소가 없으면 null을 반환한다.
    DateTime getMin();

    /// 최대값, 요소가 없으면 null을 반환한다.
    DateTime getMax();

    /// Min~Max의 기간을 나타낸다. 둘 중 하나라도 null이면 null을 반환한다.
    Duration getDuration();

    /// 요소가 없는 컬렉션인가?
    boolean isEmpty();

    /// 모든 요소가 같은 시각을 나타내는가?
    boolean isMoment();

    /// 요소가 모든 시각을 나타내는가?
    boolean isAnytime();

    /// 지정된 컬렉션의 요소들을 모두 추가합니다.
    void addAll(Iterable<DateTime> moments);

    /// 순번에 해당하는 시각들의 Duration을 구합니다.
    List<Duration> getDurations(int startIndex, int count);

    /// 지정된 시각의 바로 전의 시각을 찾습니다. 없으면 null을 반환합니다.
    DateTime findPrevious(DateTime moment);

    /// 지정된 시각의 바로 후의 시각을 찾습니다. 없으면 null을 반환합니다.
    DateTime findNext(DateTime moment);
}
