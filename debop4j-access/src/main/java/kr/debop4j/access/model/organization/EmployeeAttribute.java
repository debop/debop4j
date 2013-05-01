package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 직원의 부가 정보
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 25 오후 1:14
 */
@Entity
@Table(name = "EmployeeAttr")
@Getter
@Setter
public class EmployeeAttribute extends AnnotatedEntityBase {

    private static final long serialVersionUID = 8289321985710435869L;

    protected EmployeeAttribute() {}

    public EmployeeAttribute(Employee employee) {
        this.employee = employee;
    }

    /** Hibernate 기본의 one-to-one 입니다!!! */
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign",
                      parameters = @org.hibernate.annotations.Parameter(name = "property", value = "employee"))
    @Column(name = "EmployeeId")
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Employee employee;

    @Column(length = 1024)
    private String signImageUrl;

    @Lob
    @Basic
    private byte[] signImage;

    @Column(length = 1024)
    private String faceImageUrl;

    @Lob
    private byte[] faceImage;

    @Override
    public int hashCode() {
        return isPersisted() ? HashTool.compute(id) : HashTool.compute(employee);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("signImageUrl", signImageUrl)
                .add("faceImageUrl", faceImageUrl);
    }
}
