package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedTreeEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

/**
 * 부서 정보를 나타냅니다. (상위부서, 하위부서 등을 표현합니다)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Entity
@Table(name = "Department")
@org.hibernate.annotations.Table(appliesTo = "DEPARTMENT",
                                 indexes = {@org.hibernate.annotations.Index(name = "ix_department",
                                                                             columnNames = {
                                                                                     "CompanyName",
                                                                                     "DeptCode"})})
@org.hibernate.annotations.Cache(region = "Vital.Common", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Department extends AnnotatedTreeEntityBase<Department> implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 512869366829603899L;

    protected Department() {}

    public Department(String companyName, String departmentCode) {
        this(companyName, departmentCode, departmentCode);
    }

    public Department(String companyName, String departmentCode, String departmentName) {
        Guard.shouldNotBeEmpty(companyName, "companyName");
        Guard.shouldNotBeEmpty(departmentCode, "departmentCode");
        Guard.shouldNotBeEmpty(departmentName, "departmentName");

        this.companyName = companyName;
        this.code = departmentCode;
        this.name = departmentName;
    }

    @Id
    @GeneratedValue
    @Column(name = "DeptId")
    private Long id;

    @Column(name = "CompanyName", nullable = false, length = 64)
    private String companyName;

    @Column(name = "DeptCode", nullable = false, length = 64)
    private String code;

    @Column(name = "DeptName", nullable = false, length = 128)
    private String name;

    @Column(name = "DeptEname", length = 128)
    private String enam;

    @Column(name = "IsActive")
    private Boolean active;

    @Column(name = "DeptDesc", length = 4000)
    private String description;

    @Column(name = "ExAttr", length = 4000)
    private String exAttr;

    @OneToMany(mappedBy = "department", cascade = {CascadeType.ALL})
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.SELECT)
    @OrderBy(value = "name")
    private Set<Employee> employees = Sets.newHashSet();

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime updatedTime;

    public Date getUpdateTimestamp() {
        return updatedTime.toDate();
    }

    @Override
    public void updateUpdateTimestamp() {
        updatedTime = DateTime.now();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();
        return HashTool.compute(code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("name", name);
    }
}
