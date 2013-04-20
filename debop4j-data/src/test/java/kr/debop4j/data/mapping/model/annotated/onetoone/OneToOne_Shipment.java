package kr.debop4j.data.mapping.model.annotated.onetoone;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * org.annotated.mapping.domain.model.onetoone.OneToOne_Shipment
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 4.
 */
@Entity
@Table(name = "JPA_ONE_TO_ONE_SHIPMENT")
@SecondaryTable(name = "JPA_ONE_TO_ONE_ITEM_SHIPMENT")
@Getter
@Setter
public class OneToOne_Shipment extends JpaEntityBase {

    private static final long serialVersionUID = 7430556649473370285L;

    @Id
    @GeneratedValue
    @Column(name = "SHIPMENT_ID")
    private Long id;

    @Column(name = "SHIPMENT_STATE")
    private String state;

    @Column(name = "SHIPMENT_CREATE_ON")
    private Date createOn;

    @ManyToOne
    @JoinColumn(table = "JPA_ONE_TO_ONE_ITEM_SHIPMENT", name = "ITEM_ID")
    private OneToOne_Item auction = new OneToOne_Item();

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(state);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("state", state)
                .add("createOn", createOn);
    }
}
