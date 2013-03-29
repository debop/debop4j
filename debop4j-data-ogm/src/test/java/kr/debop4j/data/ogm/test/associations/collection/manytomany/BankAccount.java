package kr.debop4j.data.ogm.test.associations.collection.manytomany;

import com.google.common.collect.Sets;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * BankAccount
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 29. 오후 9:09
 */
@Entity
@Getter
@Setter
public class BankAccount extends AnnotatedEntityBase {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String accountNumber;

    @ManyToMany(mappedBy = "bankAccounts")
    private Set<AccountOwner> owners = Sets.newHashSet();
}
