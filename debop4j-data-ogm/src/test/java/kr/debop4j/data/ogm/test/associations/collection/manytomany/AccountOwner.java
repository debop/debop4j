package kr.debop4j.data.ogm.test.associations.collection.manytomany;

import com.google.common.collect.Sets;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
public class AccountOwner extends AnnotatedEntityBase {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String SSN;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    public Set<BankAccount> bankAccounts = Sets.newHashSet();

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(SSN);
    }
}
