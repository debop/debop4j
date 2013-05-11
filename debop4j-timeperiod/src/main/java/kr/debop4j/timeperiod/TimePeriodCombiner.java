package kr.debop4j.timeperiod;

import kr.debop4j.timeperiod.timeline.TimeLine;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.debop4j.timeperiod.TimePeriodCombiner
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오전 2:22
 */
@Slf4j
public class TimePeriodCombiner<T extends ITimePeriod> {

    public TimePeriodCombiner() {
        this(null);
    }

    public TimePeriodCombiner(ITimePeriodMapper mapper) {
        this.periodMapper = mapper;
    }

    @Getter
    private final ITimePeriodMapper periodMapper;

    public ITimePeriodCollection combinePeriods(ITimePeriod... periods) {
        return new TimeLine(new TimePeriodCollection(periods), periodMapper).combinePeriods();
    }

    public ITimePeriodCollection combinePeriods(ITimePeriodContainer periods) {
        assert periods != null;
        return new TimeLine(periods, periodMapper).combinePeriods();
    }
}
