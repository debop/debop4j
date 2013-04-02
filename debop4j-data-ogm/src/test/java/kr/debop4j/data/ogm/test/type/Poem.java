package kr.debop4j.data.ogm.test.type;

import kr.debop4j.data.ogm.test.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.UUID;

/**
 * kr.debop4j.data.ogm.test.type.Poem
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 5:12
 */
@Entity
@Getter
@Setter
public class Poem extends UuidEntityBase {

    private String name;

    private UUID poemSocietyId;

    @Temporal(TemporalType.DATE)
    private Date creation;
}
