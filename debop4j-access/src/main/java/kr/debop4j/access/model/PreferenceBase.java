package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 설정 정보의 기본 클래스
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 12.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class PreferenceBase extends AccessEntityBase {

    private static final long serialVersionUID = -6774316561760796681L;

    protected PreferenceBase() {}

    protected PreferenceBase(String key, String value) {
        Guard.shouldNotBeEmpty(key, "key");
        this.key = key;
        this.value = value;
    }

    @Column(name = "PrefKey", nullable = false, length = 255)
    private String key;

    @Column(name = "PrefValue", length = 2000)
    private String value;

    @Column(name = "DefaultValue", length = 2000)
    private String defaultValue;

    @Column(name = "Description", length = 2000)
    private String description;

    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    @Override
    public int hashCode() {
        return HashTool.compute(key);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("key", key)
                .add("value", value);
    }
}
