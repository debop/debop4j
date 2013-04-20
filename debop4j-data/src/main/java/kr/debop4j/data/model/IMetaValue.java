package kr.debop4j.data.model;

import kr.debop4j.core.IValueObject;

/**
 * 메타 정보를 표현하는 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18.
 */
public interface IMetaValue extends IValueObject {

    String getValue();

    void setValue(String value);

    String getLabel();

    void setLabel(String label);

    String getDescription();

    void setDescription(String description);

    String getExAttr();

    void setExAttr(String exAttr);
}
