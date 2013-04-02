package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * kr.debop4j.data.ogm.test.id.DistributedRevisionControl
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 2:30
 */
@Entity
@Getter
@Setter
public class DistributedRevisionControl extends AnnotatedEntityBase {

    private static final long serialVersionUID = -1994899771577067582L;

    public DistributedRevisionControl() {}

    public DistributedRevisionControl(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
