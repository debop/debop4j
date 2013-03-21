package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.core.tools.StringTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * 마스터 코드
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 21 오전 11:11
 */
@Entity
@Table
@org.hibernate.annotations.Cache(region = "Vital.Common", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class MasterCode extends VitalEntityBase {
    private static final long serialVersionUID = 3818715845269321306L;

    protected MasterCode() {}

    public MasterCode(String code, String title) {
        this.name = code;
        this.value = title;
    }

    @Id
    @GeneratedValue
    @Column(name = "CodeId")
    private Long id;

    /**
     * 마스터 코드의 이름 (ex: SR 유형)
     */
    @NotEmpty
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * 마스터 코드의 값 (ex: SR_TYPE)
     */
    @NotEmpty
    @Column(nullable = false, length = 255)
    private String value;

    /**
     * 설명
     */
    @Column(length = 2000)
    private String description;

    /**
     * 부가 특성
     */
    @Column(length = 2000)
    private String exAttr;

    /**
     * MasterCodeItem 의 집합
     */
    @OneToMany(mappedBy = "masterCode", cascade = {CascadeType.ALL})
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.SELECT)
    private Set<MasterCodeItem> items = Sets.newHashSet();

    /**
     * 새로운 {@link MasterCodeItem} 을 추가합니다.
     *
     * @param itemName  Item Name
     * @param itemValue Item Value
     */
    public void addItem(String itemName, String itemValue) {
        MasterCodeItem item = findItemByName(itemName);
        if (item == null)
            items.add(new MasterCodeItem(this, itemName, itemValue));
        else
            item.setValue(itemValue);
    }

    /**
     * ItemName 으로 {@link MasterCodeItem} 을 찾습니다.
     *
     * @param itemName 찾을 ItemName
     * @return 찾은 MasterCodeItem
     */
    public MasterCodeItem findItemByName(String itemName) {
        if (StringTool.isEmpty(itemName))
            return null;
        final String name = itemName.toLowerCase();
        for (MasterCodeItem item : items)
            if (name.equals(item.getName()))
                return item;

        return null;
    }

    /**
     * ItemValue로 {@link MasterCodeItem} 을 찾습니다.
     *
     * @param itemValue 찾을 ItemName
     * @return 찾은 MasterCodeItem
     */
    public MasterCodeItem findItemByValue(String itemValue) {
        if (StringTool.isEmpty(itemValue))
            return null;
        final String value = itemValue.toLowerCase();
        for (MasterCodeItem item : items)
            if (value.equals(item.getValue()))
                return item;
        return null;
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(name, value);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("value", value);
    }
}
