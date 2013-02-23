package kr.debop4j.data.model;

import com.google.common.collect.Maps;
import lombok.Getter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 메타데이터를 제공하는 엔티티의 추상 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 19
 */
@MappedSuperclass
public abstract class MetaEntityBase<TId extends Serializable> extends EntityBase<TId> implements IMetaEntity {

    private static final long serialVersionUID = 8802449633388271176L;

    @Getter
    private final Map<String, IMetaValue> metaMap = Maps.newLinkedHashMap();

    @Override
    public IMetaValue getMetaValue(String key) {
        return getMetaMap().get(key);
    }

    @Override
    public Set<String> getMetaKeys() {
        return getMetaMap().keySet();
    }

    @Override
    public void addMetaValue(String metaKey, IMetaValue metaValue) {
        getMetaMap().put(metaKey, metaValue);
    }

    @Override
    public void addMetaValue(String metaKey, Object value) {
        getMetaMap().put(metaKey, new SimpleMetaValue(value));
    }

    @Override
    public void removeMetaValue(String metaKey) {
        getMetaMap().remove(metaKey);
    }
}
