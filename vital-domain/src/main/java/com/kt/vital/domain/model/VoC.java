package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * com.kt.vital.domain.model.VoC
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Entity
@Table(name = "VoC")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class VoC extends VitalEntityBase {

    private static final long serialVersionUID = 4096548425145872600L;

    @Id
    @GeneratedValue
    @Column(name = "VocId")
    private Long id;

    @Column(name = "VoCNo", nullable = false, length = 24)
    @Index(name = "ix_voc_no")
    private String no;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @OneToOne(mappedBy = "voc", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private VoCContent content;


    @Override
    public int hashCode() {
        if (isPersisted())
            HashTool.compute(id);
        return HashTool.compute(no);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("no", no)
                .add("createDate", createTime);
    }
}
