package kr.debop4j.data.mapping.model.annotated.onetoone;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * kr.debop4j.data.mapping.model.annotated.onetoone.OneToOne_Biography
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 23. 오후 10:08
 */
@Entity
@Table
@Getter
@Setter
public class OneToOne_Biography extends AnnotatedEntityBase {

    private static final long serialVersionUID = -5261462229916986070L;

    /** Hibernate 기본의 one-to-one 입니다!!! */
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign",
                      parameters = @Parameter(name = "property", value = "author"))
    @Column(name = "AuthorId")
    private Long authorId;

    @OneToOne
    @PrimaryKeyJoinColumn
    private OneToOne_Author author;

    @Column(name = "Information", length = 1024)
    private String information;
}
