package kr.debop4j.search.hibernate.model;

import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * kr.debop4j.search.hibernate.model.Employee
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 25. 오후 2:21
 */
@Entity
@Indexed
@Analyzer(impl = KoreanAnalyzer.class)
@Getter
@Setter
public class Employee extends AnnotatedEntityBase {
    private static final long serialVersionUID = 8568479431657188039L;

    public Employee() {}

    public Employee(Integer id, String lastname, String dept) {
        this.id = id;
        this.lastname = lastname;
        this.dept = dept;
    }

    @Id
    @DocumentId
    private Integer id;

    @Field(store = Store.YES)
    private String lastname;

    @Field(store = Store.YES)
    private String dept;

    @Temporal(TemporalType.DATE)
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.DAY)
    private Date hireDate;

    @Override
    public int hashCode() {
        return HashTool.compute(id);
    }
}
