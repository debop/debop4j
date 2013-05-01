package kr.debop4j.data.ogm.test.associations.manytoone;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Beer
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 29. 오후 9:08
 */
@Entity
@Getter
@Setter
public class Beer extends UuidEntityBase {

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false, name = "brewery_id")
    private Brewery brewery;
}
