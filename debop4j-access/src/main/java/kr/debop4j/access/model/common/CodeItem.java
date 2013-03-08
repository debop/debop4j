package kr.debop4j.access.model.common;

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
 * 코드 항목
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 1:07
 */
@Entity
@Table(name = "CODE_ITEM")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class CodeItem extends AccessEntityBase {

    protected CodeItem() {}

    public CodeItem(Code code, String itemName, String itemValue) {
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
    private Code code;

    @Column(name = "ItemName")
    private String name;

    @Column(name = "ItemValue", length = 2000)
    private String value;


    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(code, name, value);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("codeId", code.getId())
                    .add("name", name)
                    .add("value", value);
    }
}
