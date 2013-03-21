package com.kt.vital.domain.model.history;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.VitalLogEntityBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SR 리포트 정보를 EXCEL로 다운로드한 이력
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:31
 */
@Entity
@Table(name = "ExcelExportLog")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ExcelExportLog extends VitalLogEntityBase {

    private static final long serialVersionUID = -5935426118042101053L;

    protected ExcelExportLog() {}

    public ExcelExportLog(String username, String departmentCode, String keyword) {
        this(username, departmentCode, keyword, null);
    }

    public ExcelExportLog(String username, String departmentCode, String keyword, String clientAddress) {
        this.username = username;
        this.departmentCode = departmentCode;
        this.keyword = keyword;
        this.clientAddress = clientAddress;
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
    @Column(length = 1024)
    private String keyword;

    /**
     * 사용자의 IP Address
     */
    @Column(length = 50)
    private String clientAddress;

    /**
     * Export한 시각
     */
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime exportTime = DateTime.now();

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
