package kr.debop4j.data.mapping.model.annotated.subclass;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * org.annotated.mapping.domain.model.subclass.Subclass_BankAccount
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 4.
 */
@Entity
@DiscriminatorValue("BA")
public class Subclass_BankAccount extends Subclass_BillingDetails {

    private static final long serialVersionUID = 6159765179966313199L;

    @Column(name = "BANK_ACCOUNT")
    private String account;

    @Column(name = "BANK_NAME")
    private String bankname;

    @Column(name = "BANK_SWIFT")
    private String swift;
}
