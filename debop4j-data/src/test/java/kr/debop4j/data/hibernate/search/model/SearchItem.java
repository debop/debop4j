package kr.debop4j.data.hibernate.search.model;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.*;

import javax.persistence.*;

/**
 * org.hibernate.search.model.SearchItem
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 5.
 */
@Entity
@Table(name = "SEARCH_ITEM")
@Indexed
public class SearchItem extends AnnotatedEntityBase {

    @Getter
    @Id
    @GeneratedValue
    @Column(name = "SEARCH_ITEM_ID")
    @DocumentId
    private Long id;

    @Getter
    @Setter
    @Column(name = "SEARCH_ITEM_TITLE")
    @Field                                              // indexing with tokenization
    private String title;

    @Getter
    @Setter
    @Column(name = "SEARCH_ITEM_DESC")
    @Field                                              // indexing with tokenization
    private String description;

    @Getter
    @Setter
    @Column(name = "SEARCH_ITEM_EAN")
    @Field(index = Index.NO, store = Store.YES)          // indexing without tokenization
    private String ean;

    @Getter
    @Setter
    @Column(name = "SEARCH_ITEM_IMG")
    private String imageURL;                            // not indexing (default)
}
