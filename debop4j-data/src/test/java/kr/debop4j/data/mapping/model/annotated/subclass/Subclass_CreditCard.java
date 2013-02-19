package kr.debop4j.data.mapping.model.annotated.subclass;

import javax.persistence.*;

/**
 * org.annotated.mapping.domain.model.subclass.Subclass_CreditCard
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 4.
 */
@Entity
@DiscriminatorValue("CD")
@SecondaryTable(name = "JPA_SUBCLASS_CREDIT_CARD_JOIN",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "CREDIT_CARD_ID"))
public class Subclass_CreditCard extends Subclass_BillingDetails {

    private static final long serialVersionUID = -8344985908332422466L;

    @Column(table = "JPA_SUBCLASS_CREDIT_CARD_JOIN", name = "CC_NUMBER", nullable = false)
    private String number;

    @Column(table = "JPA_SUBCLASS_CREDIT_CARD_JOIN", name = "CC_EXP_MONTH", nullable = false, length = 2)
    private String expMonth;

    @Column(table = "JPA_SUBCLASS_CREDIT_CARD_JOIN", name = "CC_EXP_YEAR", nullable = false, length = 4)
    private String expYear;
}
