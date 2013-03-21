package com.kt.vital.domain.model.statistics;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.MappedSuperclass;

/**
 * 통계 데이터의 기본 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 21 오전 11:38
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class StatisticEntityBase extends AnnotatedEntityBase {

    private static final long serialVersionUID = 6567205149886942843L;


}
