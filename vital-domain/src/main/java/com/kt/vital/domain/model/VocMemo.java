package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * com.kt.vital.domain.model.VocMemo
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Entity
@Table(name = "VocMemo")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class VocMemo extends AnnotatedEntityBase {

    private static final long serialVersionUID = -1834701471286782887L;

    protected VocMemo() {}

    public VocMemo(VoiceOfCustomer voc, String memo) {
        this.voc = voc;
        this.memo = memo;

        this.voc.setContent(this);
    }

    @Id
    @GeneratedValue
    @Column(name = "VoCContentId")
    private Long id;

    @OneToOne
    @JoinColumn(name = "VoCId")
    @Index(name = "ix_voccontent_vocid")
    private VoiceOfCustomer voc;

    @Column(name = "menu", length = 4000)
    private String memo;


    @Override
    public int hashCode() {
        if (isPersisted())
            HashTool.compute(id);
        return HashTool.compute(voc);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id);
    }
}
