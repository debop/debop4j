package com.kt.vital.domain.model.system;

import com.kt.vital.domain.model.VitalEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 알람 발행 규칙
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:14
 */
@Entity
@Table(name = "AlarmPublish")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class AlarmPublish extends VitalEntityBase {


    @Id
    @GeneratedValue
    @Column(name = "AlarmId")
    private Long id;


}
