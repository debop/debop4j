package com.kt.vital.domain.model.history;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.ActionType;
import com.kt.vital.domain.model.VitalLogEntityBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * com.kt.vital.domain.model.history.UserActionLog
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:53
 */
@Entity
@Table(name = "UserActionLog")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserActionLog extends VitalLogEntityBase {

    private static final long serialVersionUID = 2845660115811984905L;

    protected UserActionLog() { }

    public UserActionLog(String username, ActionType actionType, String sessionId) {
        this(username, actionType, sessionId, null);
    }

    public UserActionLog(String username, ActionType actionType, String sessionId, String clientAddress) {

        this.username = username;
        this.actionType = actionType;

        this.sessionId = sessionId;
        this.clientAddress = clientAddress;
    }

    /**
     * 로그인 사용자 아이디
     */
    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    /**
     * 직원 사번
     */
    @Column(name = "EmpNo", length = 50)
    private String empNo;

    /**
     * 직원 명
     */
    @Column(name = "EmpName", length = 50)
    private String empName;

    /**
     * 직원의 소속 부서
     */
    @Column(name = "DeptCode", length = 50)
    private String deptCode;

    /**
     * 직원 소속 부서 명
     */
    @Column(name = "DeptName", length = 50)
    private String deptName;

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
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(name = "ActionTime")
    private DateTime actionTime = DateTime.now();

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(getId());
        return HashTool.compute(username, actionType, actionTime);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("username", username)
                    .add("actionType", actionType)
                    .add("actionTime", actionTime)
                    .add("clientAddress", clientAddress);
    }
}
