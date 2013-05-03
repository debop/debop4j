package org.hibernate.ogm.helper.memento;

import lombok.Getter;

import java.io.Serializable;

/**
 * org.hibernate.ogm.helper.memento.CareTaker
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 9:34
 */
public class CareTaker implements Serializable {

    private static final long serialVersionUID = -4159944554341104712L;

    @Getter
    private final Memento memento;

    public CareTaker(Memento memento) {
        this.memento = memento;
    }
}
