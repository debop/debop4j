package kr.debop4j.data.mapping.northwind.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

/**
 * kr.debop4j.data.mapping.northwind.model.Category
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 23.
 */
@Entity
@Table(name = "Categories")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Category extends AnnotatedEntityBase implements IEntity<Integer> {

    protected Category() {}

    public Category(String name) {
        Guard.shouldNotBeEmpty(name, "name");
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "categoryId")
    private Integer id;

    @Column(name = "CategoryName")
    private String name;
    private String description;
    private byte[] picture;

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "category", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private List<Product> products = Lists.newArrayList();

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("description", description);
    }
}
