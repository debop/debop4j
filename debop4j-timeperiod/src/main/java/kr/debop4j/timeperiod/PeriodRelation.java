package kr.debop4j.timeperiod;

/**
 * 두 개의 {@link ITimePeriod}의 관계를 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:18
 */
public enum PeriodRelation {

    /**
     * 알 수 없음 (두개의 기간({@link ITimePeriod})이 모두 Anytime 일 경우)
     */
    NoRelation,

    /**
     * 현 {@link ITimePeriod} 이후에 대상 {@link ITimePeriod}가 있을 때
     */
    After,

    /**
     * 현 {@link ITimePeriod}의 완료 시각이 대상 {@link ITimePeriod}의 시작 시각과 같습니다.
     */
    StartTouching,

    /**
     * 현 {@link ITimePeriod} 기간 안에 대상 {@link ITimePeriod}의 시작 시각만 포함될 때
     */
    StartInside,

    /**
     * 현 {@link ITimePeriod}의 시작 시각과 대상 {@link ITimePeriod}의 시작 시각이 일치하고, 대상 {@link ITimePeriod} 가 현 {@link ITimePeriod}에 포함될 때
     */
    InsideStartTouching,

    /**
     * 현 {@link ITimePeriod}의 시작 시각과 대상 {@link ITimePeriod}의 시작 시각이 일치하고, 현 {@link ITimePeriod} 가 대상 {@link ITimePeriod}에 포함될 때
     */
    EnclosingStartTouching,

    /**
     * 현 {@link ITimePeriod}가 대상 {@link ITimePeriod} 기간에 포함될 때
     */
    Enclosing,

    /**
     * 현 {@link ITimePeriod}의 완료 시각과 대상 {@link ITimePeriod}의 완료 시각이 일치하고, 현 {@link ITimePeriod} 가 대상 {@link ITimePeriod}에 포함될 때
     */
    EnclosingEndTouching,

    /**
     * 현 {@link ITimePeriod} 기간과 대상 {@link ITimePeriod}의 기간이 일치할 때, 둘 다 Anytime이라도 ExactMath가 된다.
     */
    ExactMatch,

    /**
     * 현 기간안에 대상 기간이 내부에 포함될 때
     */
    Inside,

    /**
     * 현 기간 안에 대상 기간이 포함되는데, 완료시각만 같을 때
     */
    InsideEndTouching,

    /**
     * 현 기간 안에 대상 기간의 완료 시각만 포함될 때
     */
    EndInside,

    /**
     * 현 기간의 시작 시각이 대상 기간의 완료 시각과 일치할 때
     */
    EndTouching,

    /**
     * 대상 기간의 완료 시각이 현 기간의 시작시간 전에 있을 때
     */
    Before
}
