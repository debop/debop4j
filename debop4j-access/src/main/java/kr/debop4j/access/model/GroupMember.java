package kr.debop4j.access.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 그룹의 구성원
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 5 오후 4:26
 */
@Entity
@Table(name = "GroupMember")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class GroupMember {
}
