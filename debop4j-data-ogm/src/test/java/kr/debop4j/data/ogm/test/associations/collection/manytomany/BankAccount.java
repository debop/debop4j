package kr.debop4j.data.ogm.test.associations.collection.manytomany;

import com.google.common.collect.Sets;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
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
public class BankAccount extends UuidEntityBase {

    private static final long serialVersionUID = -1539566575343970309L;

    private String accountNumber;

    @ManyToMany(mappedBy = "bankAccounts")
    private Set<AccountOwner> owners = Sets.newHashSet();

    @Override
    public int hashCode() {
        if (getId() != null)
            return super.hashCode();
        return HashTool.compute(accountNumber);
    }
}
