package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * com.kt.vital.domain.model.UserActionHistory
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:53
 */
@Entity
@Table(name = "UserActionHistory")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserActionHistory extends VitalEntityBase {

    private static final long serialVersionUID = 2845660115811984905L;

    protected UserActionHistory() { }

    public UserActionHistory(User user, ActionType actionType, String sessionId) {
        this(user, actionType, sessionId, null);
    }

    public UserActionHistory(User user, ActionType actionType, String sessionId, String clientAddress) {
        Guard.shouldNotBeNull(user, "user");

        this.user = user;
        this.username = user.getUsername();
        if (user.getDepartment() != null)
            this.departmentCode = user.getDepartment().getCode();

        this.actionType = actionType;
        this.actionTime = new Date();

        this.sessionId = sessionId;
        this.clientAddress = clientAddress;
    }

    /**
     * History Id
     */
    @Id
    @GeneratedValue
    @Column(name = "HistoryId")
    private Long id;

    /**
     * 로그인 사용자 아이디
     */
    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    /**
     * 사용자 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    private User user;

    /**
     * 직원의 소속 부서 (안 넣어도 된다)
     */
    @Column(name = "DepartmentCode", length = 50)
    private String departmentCode;

    /**
     * 세션 Id
     */
    @Column(name = "SessionId", length = 100)
    private String sessionId;

    /**
     * 사용자의 IP Address
     */
    @Column(name = "ClientAddress", length = 50)
    private String clientAddress;

    /**
     * 사용자의 Action 종류 (로그인, 로그아웃, 검색, Export 등)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ActionType", nullable = false, length = 128)
    private ActionType actionType = ActionType.Nothing;
    /**
     * 로그인한 시각
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ActionTime")
    private Date actionTime;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(username, actionType, actionTime);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("departmentCode", departmentCode)
                    .add("username", username)
                    .add("actionType", actionType)
                    .add("actionTime", actionTime)
                    .add("clientAddress", clientAddress);
    }
}
