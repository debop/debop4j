package com.kt.vital.domain.model.system;

import com.kt.vital.domain.model.VitalEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 알람 발행 이력
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 5:10
 */
@Entity
@Table(name = "AlarmHistory")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class AlarmHistory extends VitalEntityBase {
}
