package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * SR 리포트 정보를 EXCEL로 다운로드한 이력
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:31
 */
@Entity
@Table(name = "ExportHistory")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ExportHistory extends VitalHistoryEntityBase {

    private static final long serialVersionUID = -5935426118042101053L;

    protected ExportHistory() {}

    public ExportHistory(String username, String departmentCode, String keyword) {
        this(username, departmentCode, keyword, null);
    }

    public ExportHistory(String username, String departmentCode, String keyword, String clientAddress) {
        this.username = username;
        this.departmentCode = departmentCode;
        this.keyword = keyword;
        this.clientAddress = clientAddress;
        this.exportTime = new Date();
    }

    /**
     * SR 리포트를 EXPORT한 사용자
     */
    @Column(nullable = false, length = 50)
    private String username;

    /**
     * 사용자의 소속 부서 (안 넣어도 된다)
     */
    @Column(length = 50)
    private String departmentCode;

    /**
     * SR 리포트 검색을 수행한 키워드
     */
    @Column(name = "keyword", length = 1024)
    private String keyword;

    /**
     * 사용자의 IP Address
     */
    @Column(name = "ClientAddress", length = 50)
    private String clientAddress;

    /**
     * Export한 시각
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date exportTime;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(getId());
        return HashTool.compute(username, exportTime);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("departmentCode", departmentCode)
                    .add("username", username)
                    .add("clientAddress", clientAddress)
                    .add("exportTime", exportTime);
    }
}
