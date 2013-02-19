package kr.debop4j.core.tools;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.debop4j.core.tools.JClass
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 20.
 */
@Slf4j
@Getter
@Setter
public class JClass {
    private int id;
    private String name;
    private Integer age;


    public JClass() {
        this(0, "Unknown", 100);
    }

    public JClass(int id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}