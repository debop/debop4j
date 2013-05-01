package kr.debop4j.data.mongodb.model;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * kr.debop4j.data.mongodb.model.LuckyNumberPojo
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 14. 오후 10:20
 */
@Entity
@Getter
@Setter
public class LuckyNumberPojo extends AnnotatedEntityBase {
    private static final long serialVersionUID = 1146735576992528082L;

    @Id
    private String id;

    private int luckynumber;
}
