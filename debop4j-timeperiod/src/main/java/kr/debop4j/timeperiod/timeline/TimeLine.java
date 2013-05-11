package kr.debop4j.timeperiod.timeline;

import kr.debop4j.timeperiod.*;

/**
 * kr.debop4j.timeperiod.timeline.ITimeLine
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오전 12:01
 */
public class TimeLine<T extends ITimePeriod> implements ITimeLine {

    @Override
    public ITimePeriodContainer getPeriods() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriod getLimits() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriodMapper getPeriodMapper() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriodCollection combinePeriods() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriodCollection intersectPeriods() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriodCollection calculateGaps() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
