package kr.debop4j.data.ogm.test.associations.collection.manytomany;

import com.google.common.collect.Sets;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.ogm.test.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * AccountOwner
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 29. 오후 9:09
 */
@Entity
@Getter
@Setter
public class AccountOwner extends UuidEntityBase {

    private String SSN;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    public Set<BankAccount> bankAccounts = Sets.newHashSet();

    @Override
    public int hashCode() {
        if (getId() != null)
            return super.hashCode();
        return HashTool.compute(SSN);
    }
}
