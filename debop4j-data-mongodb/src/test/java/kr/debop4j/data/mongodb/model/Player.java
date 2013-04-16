package kr.debop4j.data.mongodb.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * kr.debop4j.data.ogm.dao.Player
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오전 11:34
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Indexed
@Getter
@Setter
public class Player extends UuidEntityBase {

    private static final long serialVersionUID = 7317574732346075920L;

    @Column(name = "player_name")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Boost(1.2f)
    private String name;

    @Column(name = "player_surname")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    private String surname;

    @Column(name = "player_age")
    @NumericField
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    private int age;

    @Column(name = "player_birth")
    @Temporal(TemporalType.DATE)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    @DateBridge(resolution = Resolution.DAY)
    private Date birth;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Boost(2.0f)
    private String description = "1. 동해 물과 백두산이 마르고 닳도록," +
            "   하느님이 보우하사 우리나라 만세." +
            "(후렴) 무궁화 삼천리 화려 강산," +
            "          대한 사람 대한으로 길이 보전하세." +
            "2. 남산 위에 저 소나무 철갑을 두른 듯," +
            "   바람 서리 불변함은 우리 기상일세." +
            "3. 가을 하늘 공활한데 높고 구름 없이," +
            "   밝은 달은 우리 가슴 일편단심일세." +
            "4. 이 기상과 이 맘으로 충성을 다하여," +
            "   괴로우나 즐거우나 나라 사랑하세.";

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @IndexedEmbedded
    Set<Tournament> tournaments = Sets.newHashSet();

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("surname", surname)
                .add("birth", birth)
                .add("age", age);
    }
}
