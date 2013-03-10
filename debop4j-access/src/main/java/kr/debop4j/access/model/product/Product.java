package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.ICodeBaseEntity;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * kr.debop4j.access.model.product.Product
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 10.
 */
@Entity
@Table(name = "Product")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Product extends AccessEntityBase implements ICodeBaseEntity {

    private static final long serialVersionUID = -3955160861102847816L;

    protected Product() {}

    public Product(String code) {
        this(code, code);
    }

    public Product(String code, String name) {
        Guard.shouldNotBeEmpty(code, "code");
        Guard.shouldNotBeEmpty(name, "name");
        this.code = code;
        this.name = name;
    }


    @Id
    @GeneratedValue
    @Column(name = "ProductId")
    private Long id;

    @Column(name = "ProductCode", nullable = false, length = 128)
    @Index(name = "ix_product_code")
    private String code;

    @Column(name = "ProductName", nullable = false, length = 255)
    @Index(name = "ix_product_code")
    private String name;

    @Column(name = "ProductAbbrName", nullable = false, length = 255)
    private String abbrName;

    @Column(name = "ProductActive")
    private Boolean active;

    @Column(name = "ProductDesc", length = 4000)
    private String description;

    @Column(name = "ProductExAttr", length = 4000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("name", name)
                .add("abbrName", abbrName)
                .add("active", active);
    }
}
