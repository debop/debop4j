package kr.debop4j.search.hibernate.model;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;

/**
 * kr.debop4j.search.hibernate.model.SearchItem
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 28.
 */
@Entity
@Table(name = "SearchItem")
@Indexed
@Getter
@Setter
public class SearchItem extends AnnotatedEntityBase {

    private static final long serialVersionUID = -4795821070108007345L;

    @Id
    @GeneratedValue
    @Column(name = "SEARCH_ITEM_ID")
    @DocumentId
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(name = "SEARCH_ITEM_TITLE")
    @Field(store = Store.YES)                                              // indexing with tokenization
    private String title;

    @Column(name = "SEARCH_ITEM_DESC", length = 4000)
    @Field(store = Store.YES)                                             // indexing with tokenization
    private String description;

    @Column(name = "SEARCH_ITEM_EAN")
    private String ean;

    @Column(name = "SEARCH_ITEM_IMG")
    private String imageURL;                            // not indexing (default)
}
