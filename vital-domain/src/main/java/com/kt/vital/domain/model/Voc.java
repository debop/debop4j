package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;
import java.util.Set;

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

    @Id
    @GeneratedValue
    @Column(name = "VocId")
    private Long id;

    @Column(name = "rowId", nullable = false, length = 24)
    private String rowId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(nullable = false, length = 64)
    private String creatorId;

    @Column(nullable = false, length = 64)
    private String ownerId;


    /**
     * 메모 내용
     */
    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "customerId", column = @Column(name = "CustomerId", length = 128, table = "VocMemo")),
                    @AttributeOverride(name = "body", column = @Column(name = "Body", length = 4000, table = "VocMemo"))
            })
    @Fetch(FetchMode.SELECT)
    private VocMemo memo = new VocMemo();

    /**
     * VoC 특성들
     */
    @OneToMany(mappedBy = "voc", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.SELECT)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OrderColumn(name = "AttrName")
    private Set<VocAttribute> attrs = Sets.newHashSet();

    public void addAttribute(String attrName, String attrValue) {
        VocAttribute vocAttr = new VocAttribute(this, attrName, attrValue);
        attrs.add(vocAttr);
    }

    public void removeAttribute(String attrName) {
        VocAttribute attr = findAttribute(attrName);
        if (attr != null)
            attrs.remove(attr);
    }

    public VocAttribute findAttribute(String attrName) {
        for (VocAttribute attr : attrs) {
            if (attr.getName().equals(attrName))
                return attr;
        }
        return null;
    }

    /**
     * 메타 정보
     */
    @CollectionTable(name = "VocMeta", joinColumns = {@JoinColumn(name = "VocId")})
    @MapKeyClass(String.class)
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.EXTRA)
    private final Map<String, String> metaMap = Maps.newLinkedHashMap();

    public String getMetaValue(String key) {
        return getMetaMap().get(key);
    }

    public Set<String> getMetaKeys() {
        return getMetaMap().keySet();
    }

    public void addMetaValue(String metaKey, String metaValue) {
        getMetaMap().put(metaKey, metaValue);
    }

    public void removeMetaValue(String metaKey) {
        getMetaMap().remove(metaKey);
    }

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
                    .add("createDate", createdTime);
    }
}
