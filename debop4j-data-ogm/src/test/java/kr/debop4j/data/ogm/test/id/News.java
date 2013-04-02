package kr.debop4j.data.ogm.test.id;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * kr.debop4j.data.ogm.test.id.News
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 2:21
 */
@Entity
@Table(name = "News")
@Getter
@Setter
public class News extends AnnotatedEntityBase {

    private static final long serialVersionUID = 1197914997211364282L;

    public News() {}

    public News(NewsID newsId, String content, List<Label> labels) {
        this.newsId = newsId;
        this.content = content;
        this.labels = labels;
    }

    //@EmbeddedId
    @Id
    private NewsID newsId;

    private String content;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumns({ @JoinColumn(name = "news_topic_fk", referencedColumnName = "newsid.title", nullable = false),
                         @JoinColumn(name = "news_author_fk", referencedColumnName = "newsid.author", nullable = false) })
    @JoinTable(name = "Label")
    private List<Label> labels;

    @Override
    public int hashCode() {
        return HashTool.compute(newsId, content, labels);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("newsId", newsId)
                .add("content", content)
                .add("labels", labels);
    }
}
