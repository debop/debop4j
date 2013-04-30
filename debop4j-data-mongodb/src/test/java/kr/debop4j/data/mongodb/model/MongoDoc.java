package kr.debop4j.data.mongodb.model;

import com.google.common.base.Objects;
import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.mongodb.model.MongoDoc
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 17. 오후 4:11
 */
@Entity
@Indexed
@Getter
@Setter
@Analyzer(impl = CJKAnalyzer.class)
public class MongoDoc extends UuidEntityBase {
    private static final long serialVersionUID = -1795981911420947049L;

    public static final String DefaultBody =
            "1. 동해 물과 백두산이 마르고 닳도록, 하느님이 보우하사 우리나라 만세." +
                    "   (후렴) 무궁화 삼천리 화려 강산, 대한 사람 대한으로 길이 보전하세." +
                    "2. 남산 위에 저 소나무 철갑을 두른 듯, 바람 서리 불변함은 우리 기상일세." +
                    "3. 가을 하늘 공활한데 높고 구름 없이, 밝은 달은 우리 가슴 일편단심일세." +
                    "4. 이 기상과 이 맘으로 충성을 다하여, 괴로우나 즐거우나 나라 사랑하세.";

    public MongoDoc() {}

    public MongoDoc(String name) {
        this.name = name;
        this.body = DefaultBody;
    }

    @Field(store = Store.YES)
    private String name;

    @Field(store = Store.YES)
    @Analyzer(impl = KoreanAnalyzer.class)
    private String body;


    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("body", body);
    }
}
