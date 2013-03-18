package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * com.kt.vital.domain.model.VoCContent
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Entity
@Table(name = "VoCContent")
@Getter
@Setter
public class VoCContent extends VitalEntityBase {

    private static final long serialVersionUID = -1834701471286782887L;

    protected VoCContent() {}

    public VoCContent(VoC voc, String memo) {
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
    private VoC voc;

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
