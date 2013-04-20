package kr.debop4j.data.model;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 트리 구조를 가지며, 메타정보를 제공하는 엔티티
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 19
 */
public abstract class TreeMetaEntityBase<T extends IEntity<TId> & ITreeEntity<T>, TId extends Serializable>
        extends TreeEntityBase<T, TId> implements IMetaEntity {

    private static final long serialVersionUID = -7475565290976767358L;

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
