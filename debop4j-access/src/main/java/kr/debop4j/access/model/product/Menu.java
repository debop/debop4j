/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.ITreeEntity;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import kr.debop4j.data.model.TreeNodePosition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * 메뉴 정보를 나타냅니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 11.
 */
@Entity
@Table( name = "Menu" )
@org.hibernate.annotations.Cache( region = "Product", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Menu extends AnnotatedEntityBase implements ITreeEntity<Menu>, IUpdateTimestampedEntity {
    private static final long serialVersionUID = -8474291594296677585L;


    @Id
    @GeneratedValue
    @Column( name = "MenuId" )
    private Long id;

    @ManyToOne
    @JoinColumn( name = "ProductId", nullable = false )
    @Index( name = "ix_menu_code" )
    private Product product;

    /** Company 가 null이면, Product 의 기본 메뉴라고 볼 수 있다. */
    @ManyToOne
    @JoinColumn( name = "CompanyId" )
    @Index( name = "ix_menu_code" )
    private Company company;

    @Column( name = "MenuCode", nullable = false, length = 128 )
    @Index( name = "ix_menu_code" )
    private String code;

    @Column( name = "MenuTitle", nullable = false, length = 255 )
    private String title;

    @Column( name = "MenuUrl", length = 1024 )
    private String url;

    @Column( name = "Active" )
    private Boolean active;

    @Column( name = "Description", length = 2000 )
    private String description;

    @Column( name = "ExAttr", length = 2000 )
    private String exAttr;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "ParentId" )
    @ForeignKey( name = "fk_menu_parent" )
    private Menu parent;

    @Setter( AccessLevel.PROTECTED )
    @OneToMany( mappedBy = "parent", cascade = { CascadeType.ALL } )
    @LazyCollection( LazyCollectionOption.EXTRA )
    private Set<Menu> children = Sets.newLinkedHashSet();

    @Embedded
    private TreeNodePosition nodePosition = new TreeNodePosition();

    @Type( type = "kr.debop4j.data.hibernate.usertype.JodaDateTimeUserType" )
    private DateTime updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = DateTime.now();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(product, company, code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("title", title)
                .add("url", url)
                .add("active", active)
                .add("product", product)
                .add("company", company);
    }
}

