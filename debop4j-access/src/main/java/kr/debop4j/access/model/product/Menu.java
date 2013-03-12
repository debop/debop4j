package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedTreeEntityBase;
import kr.debop4j.data.model.ITreeEntity;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * 메뉴 정보를 나타냅니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
@Entity
@Table(name = "Menu")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Menu extends AnnotatedTreeEntityBase<Menu> implements ITreeEntity<Menu>, IUpdateTimestampedEntity {
    private static final long serialVersionUID = -8474291594296677585L;


    @Id
    @GeneratedValue
    @Column(name = "MenuId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId", nullable = false)
    @Index(name = "ix_menu_code")
    private Product product;

    /**
     * Company 가 null이면, Product 의 기본 메뉴라고 볼 수 있다.
     */
    @ManyToOne
    @JoinColumn(name = "CompanyId")
    @Index(name = "ix_menu_code")
    private Company company;

    @Column(name = "MenuCode", nullable = false, length = 128)
    @Index(name = "ix_menu_code")
    private String code;

    @Column(name = "MenuTitle", nullable = false, length = 255)
    private String title;

    @Column(name = "MenuUrl", length = 1024)
    private String url;

    @Column(name = "Active")
    private Boolean active;

    @Column(name = "Description", length = 2000)
    private String description;

    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(product, company, code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("title", title)
                .add("url", url)
                .add("active", active)
                .add("product", product)
                .add("company", company);
    }
}

