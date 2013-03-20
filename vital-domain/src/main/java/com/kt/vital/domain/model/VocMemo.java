package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Embeddable;

/**
 * com.kt.vital.domain.model.VocMemo
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Embeddable
@Getter
@Setter
public class VocMemo extends ValueObjectBase {

    private static final long serialVersionUID = -1834701471286782887L;

    /**
     * 고객 전화번호
     */
    @NotEmpty
    private String customerNo;

    /**
     * 상담 메모
     */
    private String body;

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("customerNo", customerNo)
                .add("body", body);
    }
}
