package kr.debop4j.data.ogm.test.jpa;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.jpa.Poem
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 12. 오후 1:27
 */
@Entity
@Getter
@Setter
public class Poem extends UuidEntityBase {

    private static final long serialVersionUID = 5218728315238174434L;

    public String name;
}
