package kr.debop4j.data.ogm.test.perf;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * kr.debop4j.data.ogm.test.perf.Author
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Author extends AnnotatedEntityBase {

    @Id
    private Integer id;

    private String fname;
    private String lname;
    private String mname;

    private Date dob;
    private String bio;
}
