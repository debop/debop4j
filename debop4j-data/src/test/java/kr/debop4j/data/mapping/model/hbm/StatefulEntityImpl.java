package kr.debop4j.data.mapping.model.hbm;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;


/**
 * JPA 기본 엔티티 예
 * Jpa@author 배성혁 ( sunghyouk.bae@gmail.com )
 *
 * @since 12. 11. 19
 */
@Slf4j
@Getter
@Setter
@Entity
@Table(name = "STATE_ENTITY")
public class StatefulEntityImpl extends JpaEntityBase {

    private static final long serialVersionUID = 6927281191366376283L;

    protected StatefulEntityImpl() {
    }

    public StatefulEntityImpl(final String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "ENTITY_ID")
    private Long id;


    @Column(name = "STATE_NAME", nullable = false, length = 128)
    @org.hibernate.annotations.Index(name = "IX_STATE_ENTITY_NAME")
    //@Access(value=AccessType.FIELD)
    private String name;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED", insertable = false, updatable = false)
    @Generated(value = GenerationTime.ALWAYS)
    @Getter
    private Date lastUpdated;

    @PrePersist
    @PreUpdate
    protected void updateLastUpdated() {

        if (StatefulEntityImpl.log.isDebugEnabled())
            StatefulEntityImpl.log.debug("PrePersist, PreUpdate event 발생...");

        lastUpdated = new Date();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return Objects.hashCode(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("lastUpdated", lastUpdated);
    }
}
