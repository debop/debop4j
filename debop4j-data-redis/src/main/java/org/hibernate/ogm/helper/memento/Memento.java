package org.hibernate.ogm.helper.memento;

import lombok.Getter;

import java.io.Serializable;

/**
 * org.hibernate.ogm.helper.obj.Memento
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 9:31
 */
public class Memento implements Serializable {

    private static final long serialVersionUID = 5225568926291199179L;

    @Getter
    private final Object obj;

    public Memento(Object memento) {
        this.obj = memento;
    }
}
