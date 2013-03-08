package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 사용자 정보
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 5:12
 */
@Entity
@Table(name = "Users")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class User extends Employee {

    @Column(name = "username", nullable = false, length = 32)
    @Index(name = "ix_user_username")
    private String username;

    @Column(name = "password", nullable = false, length = 128)
    @Index(name = "ix_user_username")
    private String password;


    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();
        return HashTool.compute(super.hashCode(), username, password);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("username", username)
                    .add("password", password);
    }

}
