package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * VoC의 특성을 나타내는 코드들입니다. (업무유형,
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19 오후 3:09
 */
@Entity
@Table(name = "VocAttr")
@org.hibernate.annotations.Table(appliesTo = "VocAttr",
                                 indexes = {@Index(name = "IX_VocAttr_Name",
                                                   columnNames = {"VocId", "AttrName"})})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class VocAttribute extends AnnotatedEntityBase {

    private static final long serialVersionUID = 647972570883621438L;

    protected VocAttribute() {}

    public VocAttribute(Voc voc, String attrName, String attrValue) {
        this.name = attrName;
        this.value = value;
        this.setVoc(voc);
    }

    @Id
    @GeneratedValue
    @Column(name = "AttrId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "VocId")
    private Voc voc;

    @Column(name = "AttrName", nullable = false, length = 256, updatable = false)
    private String name;

    @Column(name = "AttrValue", nullable = false, length = 1024)
    private String value;

    @Override
    public int hashCode() {
        if (isPersisted())
            HashTool.compute(id);
        return HashTool.compute(voc, name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("name", name)
                    .add("value", value);
    }
}
