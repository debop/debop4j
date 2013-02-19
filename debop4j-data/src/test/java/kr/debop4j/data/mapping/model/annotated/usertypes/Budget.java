package kr.debop4j.data.mapping.model.annotated.usertypes;

import kr.debop4j.data.jpa.domain.JpaEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * org.annotated.mapping.domain.model.usertypes.Budget
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 6.
 */

@Getter
@Setter
@Entity
@Table(name = "JPA_USERTYPE_BUDGET")
@DynamicInsert
@DynamicUpdate
public class Budget extends JpaEntityBase {

    private static final long serialVersionUID = -2666185706211853371L;

    @Id
    @GeneratedValue
    @Column(name = "BUGET_ID")
    private Long id;

    @Type(type = "kr.debop4j.data.mapping.model.annotated.usertypes.MonetaryAmountUserType")
    @Column(name = "INITIAL_PRICE", nullable = true)
    private MonetaryAmount initialPrice;


    @Type(type = "kr.debop4j.data.mapping.model.annotated.usertypes.MonetaryAmountCompositeUserType")
    @Columns(columns =
                     {
                             @Column(name = "COMPOSITE_AMOUNT"),
                             @Column(name = "COMPOSITE_CURRENCY", length = 8)
                     })
    private MonetaryAmount compositePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "RATING", nullable = false, length = 128)
    private Rating rating;
}
