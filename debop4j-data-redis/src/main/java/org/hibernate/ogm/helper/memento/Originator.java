package org.hibernate.ogm.helper.memento;

import lombok.Getter;

import java.io.Serializable;

/**
 * org.hibernate.ogm.helper.memento.Originator
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 9:33
 */
public class Originator implements Serializable {

    private static final long serialVersionUID = 7253744003023137521L;

    @Getter
    private Object obj;

    public Memento createMemento(Object obj) {
        return new Memento(obj);
    }

    /**
     * 작업을 취소하고, 기존 값으로 복구합니다.
     */
    public void undo(Memento memento) {
        obj = memento.getObj();
    }
}
