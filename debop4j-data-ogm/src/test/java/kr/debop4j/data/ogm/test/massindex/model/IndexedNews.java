package kr.debop4j.data.ogm.test.massindex.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.ogm.test.id.NewsID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;

/**
 * kr.debop4j.data.ogm.test.massindex.model.IndexedNews
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 1:16
 */
@Entity
@Indexed
@Getter
@Setter
public class IndexedNews extends AnnotatedEntityBase {

    public IndexedNews() {}

    public IndexedNews(NewsID newsId, String content) {
        this.newsId = newsId;
        this.content = content;
    }

    @Id
    @DocumentId
    @EmbeddedId
    @FieldBridge(impl = NewsIdFieldBridge.class)
    private NewsID newsId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumns({ @JoinColumn(name = "news_topic_fk", referencedColumnName = "newsid.title", nullable = false),
                         @JoinColumn(name = "news_author_fk", referencedColumnName = "newsid.author", nullable = false) })
    private List<IndexedLabel> labels = Lists.newArrayList();

    private String content;

    @Override
    public int hashCode() {
        return HashTool.compute(newsId, content);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("newsId", newsId)
                .add("content", content)
                .add("lavels", labels);
    }
}
