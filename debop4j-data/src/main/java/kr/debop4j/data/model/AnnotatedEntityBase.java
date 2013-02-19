package kr.debop4j.data.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;

/**
 * Annotation 기반의 Hibernate Entity를 표현한 추상 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 7.
 */
@DynamicInsert
@DynamicUpdate
public abstract class AnnotatedEntityBase extends StatefulEntityBase {

    private static final long serialVersionUID = -9112420541378011322L;

    @PostPersist
    public final void postPersist() {
        onPersist();
    }

    @PostLoad
    public final void postLoad() {
        onLoad();
    }
}
