package kr.debop4j.data.mongodb.model;

import com.google.common.collect.Sets;
import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * kr.debop4j.data.mongodb.model.Tournament
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 16. 오후 5:52
 */
@Entity
@Indexed
@Getter
@Setter
@Analyzer(impl = CJKAnalyzer.class)
public class Tournament extends UuidEntityBase {
    private static final long serialVersionUID = 3431486937664639007L;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String tournament;

    @ManyToMany(mappedBy = "tournaments", fetch = FetchType.EAGER)
    @ContainedIn
    Set<Player> players = Sets.newHashSet();

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Boost(1.2f)
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
}
