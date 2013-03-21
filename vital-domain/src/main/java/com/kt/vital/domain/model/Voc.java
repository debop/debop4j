package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

/**
 * com.kt.vital.domain.model.Voc
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Entity
@Table(name = "Voc")
@SecondaryTable(name = "VocMemo", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "VocId")})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "Voc",
                                 indexes = {@org.hibernate.annotations.Index(name = "ix_voc_creator",
                                                                             columnNames = {
                                                                                     "rowId",
                                                                                     "creatorId"})})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Voc extends AnnotatedEntityBase {

    private static final long serialVersionUID = 4096548425145872600L;

    protected Voc() {}

    public Voc(String rowId, DateTime createdTime, String creatorId, String ownerId) {
        this.rowId = rowId;
        this.createdTime = createdTime;
        this.creatorId = creatorId;
        this.ownerId = ownerId;
    }

    @Id
    @GeneratedValue
    @Column(name = "VocId")
    private Long id;

    @Column(name = "RowId", nullable = false, length = 24)
    private String rowId;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime createdTime;

    @Column(nullable = false, length = 64)
    private String creatorId;

    @Column(nullable = false, length = 64)
    private String ownerId;

    // TODO: 현재는 join 방식인데, lazy fetching 이 안되면, one-to-one으로 변환해야 한다.
    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "customerId",
                                       column = @Column(name = "CustomerId", length = 128, table = "VocMemo")),
                    @AttributeOverride(name = "body",
                                       column = @Column(name = "Body", length = 2000, table = "VocMemo"))
            })
    @Fetch(FetchMode.SELECT)
    @Basic(fetch = FetchType.LAZY)
    private VocMemo memo = new VocMemo();

    /**
     * VoC 특성들
     */
    @CollectionTable(name = "VocAttr", joinColumns = @JoinColumn(name = "VocId"))
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @MapKeyColumn(name = "AttrName", nullable = false, length = 128)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Column(name = "AttrValue", length = 2000)
    private Map<String, String> attrs = Maps.newHashMap();

    @Override
    public int hashCode() {
        if (isPersisted())
            HashTool.compute(id);
        return HashTool.compute(rowId);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("no", rowId)
                .add("createdTime", createdTime);
    }
}
