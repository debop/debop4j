package kr.debop4j.data.mapping.model.hbm.collection;

import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.EntityBase;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * org.hibernate.mapping.domain.model.collection.Item
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 2.
 */
@Getter
@Setter
public class Item extends EntityBase<Long> {

    private static final long serialVersionUID = -8817292400220149595L;

    private String name;
    private String description;
    private BigDecimal initialPrice;
    private BigDecimal reservePrice;
    private Date startDate;
    private Date endDate;

    private ItemState state;

    private Date approvalDate;

    private Set<Image> images = new HashSet<Image>();

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return HashTool.compute(name);
    }
}
