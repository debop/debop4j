package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 특정 회사에서 사용하는 코드 항목
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 1:07
 */
@Entity
@Table(name = "CompanyCodeItem")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class CompanyCodeItem extends AccessEntityBase {

    private static final long serialVersionUID = 2235164451774108012L;

    protected CompanyCodeItem() {}

    public CompanyCodeItem(CompanyCode code, String itemName, String itemValue) {
        Guard.shouldNotBeNull(code, "code");
        Guard.shouldNotBeEmpty(itemValue, "itemValue");

        this.code = code;
        this.name = itemName;
        this.value = itemValue;

        this.code.getItems().add(this);
    }

    @Id
    @GeneratedValue
    @Column(name = "CodeItemId")
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CodeId", nullable = false)
    private CompanyCode code;

    @Column(name = "ItemName", nullable = false, length = 128)
    private String name;

    @Column(name = "ItemValue", length = 2000)
    private String value;

    @Column(name = "Descripton", length = 2000)
    private String description;

    //@Basic(fetch = FetchType.LAZY)
    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(code, name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("value", value);
    }
}
