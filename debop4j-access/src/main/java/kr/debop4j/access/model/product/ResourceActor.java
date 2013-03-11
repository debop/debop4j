package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.Set;

/**
 * 리소스에 대한 사용자의 접근 권한을 나타냅니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
@Entity
@Table(name = "ResourceActor")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ResourceActor extends AccessEntityBase {

    private static final long serialVersionUID = -3295576813713062708L;

    protected ResourceActor() {}

    public ResourceActor(Resource resource, Company company, ActorKind actorKind, String actorCode) {
        Guard.shouldNotBeNull(resource, "resource");
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(actorCode, "actorCode");

        this.resource = resource;
        this.company = company;
        this.actorKind = actorKind;
        this.actorCode = actorCode;
    }

    @Id
    @GeneratedValue
    @Column(name = "ResourceActorId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Index(name = "ix_resource_actor")
    private Resource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @Index(name = "ix_resource_actor")
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "ActorKind", nullable = false, length = 128)
    @Index(name = "ix_resource_actor")
    private ActorKind actorKind = ActorKind.User;

    @Column(name = "ActorCode", nullable = false, length = 128)
    @Index(name = "ix_resource_actor")
    private String actorCode;

    // one-to-many 이면서 child가 component 인 경우!!!
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ResourceActorAuths", joinColumns = @JoinColumn(name = "ResourceActorId"))
    @Column(name = "AuthorityKind", nullable = false, length = 128)
    private Set<AuthorityKind> authorityKinds = EnumSet.noneOf(AuthorityKind.class);

    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(resource, company, actorKind, actorCode);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("actorKind", actorKind)
                .add("actorCode", actorCode)
                .add("authorityKinds", authorityKinds)
                .add("resource", resource)
                .add("company", company);
    }
}
