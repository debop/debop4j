package kr.debop4j.data.ogm.test.hsearch;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.hsearch.Insurance
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
@Entity
@Indexed
@Getter
@Setter
public class Insurance extends UuidEntityBase {

    @Field
    private String name;
}
