package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * kr.debop4j.access.model.product.ProductCodeItem
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 12.
 */
@Entity
@Table(name = "ProductCodeItem")
@org.hibernate.annotations.Cache(region = "Product", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ProductCodeItem extends AccessEntityBase {

    protected static final long serialVersionUID = -9119847751688766338L;

    protected ProductCodeItem() {}

    public ProductCodeItem(ProductCode code, String itemName, String itemValue) {
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

    @ManyToOne
    @JoinColumn(name = "CodeId", nullable = false)
    private ProductCode code;

    @Column(name = "ItemName", nullable = false, length = 128)
    private String name;

    @Column(name = "ItemValue", length = 2000)
    private String value;

    @Column(name = "Description", length = 2000)
    private String descripton;

    // @Basic(fetch = FetchType.LAZY)
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
