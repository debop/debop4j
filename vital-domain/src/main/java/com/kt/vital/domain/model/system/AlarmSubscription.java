package com.kt.vital.domain.model.system;

import com.kt.vital.domain.model.VitalEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 알람 발행에 대한 구독 정보 - 해당 알람에 대해 여기에 등록된 사용자들에게 알람을 보낸다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 5:08
 */
@Entity
@Table(name = "AlarmSubscription")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class AlarmSubscription extends VitalEntityBase {
}
