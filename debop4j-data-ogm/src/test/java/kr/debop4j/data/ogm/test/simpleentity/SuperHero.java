package kr.debop4j.data.ogm.test.simpleentity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.simpleentity.SuperHero
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class SuperHero extends Hero {

    private String specialPower;
}