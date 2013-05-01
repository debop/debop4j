package kr.debop4j.data.mapping.model.annotated.onetoone;

import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * kr.debop4j.data.mapping.model.annotated.onetoone.OneToOne_Author
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 23. 오후 10:08
 */
@Entity
@Table
@Getter
@Setter
public class OneToOne_Author extends AnnotatedEntityBase {

    private static final long serialVersionUID = 7068044349471017755L;

    @Id
    @GeneratedValue
    @Column(name = "AuthorId")
    private Long id;

    @Column(name = "AuthorName", nullable = false, length = 128)
    private String name;

    @OneToOne(mappedBy = "author", cascade = { CascadeType.ALL })
    private OneToOne_Biography biography;

    @Override
    public int hashCode() {
        return (isPersisted()) ? HashTool.compute(id) : HashTool.compute(name);
    }
}
