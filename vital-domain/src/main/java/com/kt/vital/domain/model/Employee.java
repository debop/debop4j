package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * Voc 담당 직원 (KT 또는 협력업체 직원)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19 오후 12:17
 */
@Entity
@Table(name = "Employee")
@org.hibernate.annotations.Cache(region = "Vital.Common", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Employee extends VitalEntityBase {

    private static final long serialVersionUID = 203404211452671575L;

    protected Employee() {}

    public Employee(String name) {
        Guard.shouldNotBeEmpty(name, "name");
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "EmpId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DeptId")
    private Department department;

    /**
     * SR 로그인 ID
     */
    @Column(name = "LoginId", length = 50)
    @Index(name = "ix_employee")
    private String loginId;

    /**
     * 직원 명
     */
    @Column(name = "EmpName", length = 50)
    @Index(name = "ix_employee")
    private String name;

    /**
     * 직원 사번
     */
    @Column(name = "EmpNo", length = 50)
    @Index(name = "ix_employee_empNo")
    private String empNo;

    /**
     * 설명
     */
    @Column(name = "EmpDesc", length = 2000)
    private String description;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(name, loginId);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("loginId", loginId)
                    .add("name", name);
    }
}
