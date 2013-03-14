package kr.debop4j.access.model.workcalendar;

import java.util.Date;

/**
 * 특정 단위 시각의 작업 시간에 대한 정보의 인터페이스 (일단위, 시간단위, 분단위, 5분단위, 월단위, 주단위 등 모두 가능하다)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
public interface IWorkTimeByTime {

    /**
     * Work Time 계산의 기준이 되는 Calendar 정보
     */
    WorkCalendar getWorkCalendar();

    /**
     * 기준 작업 시각
     */
    Date getWorkTime();

    /**
     * IsWorking
     *
     * @return
     */
    Boolean getIsWorking();

    /**
     * 지정된 WorkTime의 작업시간을 분단위로 환산해서 표현함
     */
    Integer getWorkInMinute();

    /**
     * 작업시간의 누적시간을 분단위로 표시 (시작시각과 소요작업시간만 알면 완료 시각을 빠르게 알 수 있도록 하기 위해 미리 계산함)
     */
    Long getCumulatedInMinute();

    void setCumulatedInMinute(Long cumulatedInMunute);
}
