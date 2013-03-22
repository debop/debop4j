package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Vital 시스템 사용자 정보 (Voc 담당 직원 정보는 {@link Employee} 입니다)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 3:58
 */
@Entity
@Table(name = "Users")
@org.hibernate.annotations.Cache(region = "Vital.Common", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class User extends VitalEntityBase {

    private static final long serialVersionUID = -4012904720900704159L;

    protected User() {}

    public User(String username, String passwd) {
        Guard.shouldNotBeEmpty(username, "username");
        Guard.shouldNotBeEmpty(passwd, "passwd");

        this.username = username;
        this.passwd = passwd;
        this.enabled = true;
    }

    @Id
    @GeneratedValue
    @Column(name = "UserId")
    private Long id;

    /**
     * 시스템 사용자 명 (로그인 id)
     */
    @NotNull
    @Column(nullable = false, length = 50)
    @Index(name = "ix_user_username")
    private String username;

    @NotNull
    @Column(nullable = false, length = 50)
    private String passwd;

    /**
     * 사용자가 속한 롤 : null 이면 기본 role을 가진다고 가정한다.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "RoleId")
    private Role role;

    /**
     * 사용자의 실제 직원 정보
     */
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "EmployeeId")
    private Employee employee;

    @Basic
    private Boolean enabled;

    /**
     * 사용자의 이름
     */
    @Column(length = 100)
    private String name;

    /**
     * 사용자 email
     */
    @Email
    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 50)
    private String mobile;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime enabledTime;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(username);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("username", username)
                    .add("name", name)
                    .add("email", email)
                    .add("phone", phone);
    }
}
