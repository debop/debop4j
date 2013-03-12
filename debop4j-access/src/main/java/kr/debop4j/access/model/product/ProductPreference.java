package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.PreferenceBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 제품의 설정 정보
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 12.
 */
@Entity
@Table(name = "ProductPreference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ProductPreference extends PreferenceBase {

    private static final long serialVersionUID = -8362998094989974826L;

    protected ProductPreference() {}

    public ProductPreference(Product product, String key, String value) {
        super(key, value);
        this.product = product;
    }

    @Id
    @GeneratedValue
    @Column(name = "PreferenceId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    @Index(name = "ix_productPreference")
    private Product product;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(product, getKey());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("key", getKey())
                .add("value", getValue());
    }
}
