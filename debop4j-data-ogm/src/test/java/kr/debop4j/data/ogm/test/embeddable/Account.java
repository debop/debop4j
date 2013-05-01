package kr.debop4j.data.ogm.test.embeddable;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * kr.debop4j.data.ogm.test.embeddable.Account
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Account extends AnnotatedEntityBase {

    @Id
    private String login;

    private String password;

    @Embedded
    private Address homeAddress = new Address();

}
