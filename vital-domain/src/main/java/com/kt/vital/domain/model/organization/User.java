package com.kt.vital.domain.model.organization;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.VitalEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * com.kt.vital.domain.model.organization.User
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 3:58
 */
@Entity
@Table(name = "User")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class User extends VitalEntityBase {

    protected User() {}

    public User(String username, String passwd) {
        Guard.shouldNotBeEmpty(username, "username");
        Guard.shouldNotBeEmpty(passwd, "passwd");

        this.username = username;
        this.passwd = passwd;
    }

    @Id
    @GeneratedValue
    @Column(name = "UserId", nullable = false)
    private Long id;

    /**
     * 시스템 사용자 명 (로그인 id)
     */
    @Column(nullable = false, length = 50)
    @Index(name = "ix_user_username")
    private String username;

    @Column(nullable = false, length = 50)
    private String passwd;

    /**
     * 사용자가 속한 롤 : null 이면 기본 role을 가진다고 가정한다.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "RoleId")
    private Role role;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "DeptId")
    private Department department;

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
    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 50)
    private String mobile;

    @Temporal(TemporalType.TIMESTAMP)
    private String enabledTime;

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
