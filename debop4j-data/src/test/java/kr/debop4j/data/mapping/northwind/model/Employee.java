package kr.debop4j.data.mapping.northwind.model;


import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * kr.debop4j.data.mapping.northwind.model.Employee
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 23.
 */
@Entity
@Table(name = "Employees")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Employee extends AnnotatedEntityBase implements IEntity<Integer> {

    protected Employee() {}

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employeeId")
    private Integer id;

    private String lastName;
    private String firstName;

    private String title;
    private String titleOfCourtesy;
    private Date birthDate;
    private Date hireDate;

    @Embedded
    private AddressInfo addressInfo = new AddressInfo();

    private String homePhone;
    private String extension;

    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportsTo")
    private Employee reportsTo;

    @OneToMany(mappedBy = "reportsTo", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Employee> staffMembers = Lists.newArrayList();

    @OneToMany(mappedBy = "seller", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Order> orders = Lists.newArrayList();


    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(lastName, firstName);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("lastName", lastName)
                .add("firstName", firstName);
    }
}
