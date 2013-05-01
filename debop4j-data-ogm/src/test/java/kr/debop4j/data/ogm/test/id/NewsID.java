package kr.debop4j.data.ogm.test.id;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

/**
 * kr.debop4j.data.ogm.test.id.NewsID
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 1:21
 */
@Embeddable
@Getter
@Setter
public class NewsID extends ValueObjectBase {

    private static final long serialVersionUID = 8795401732909599495L;

    public NewsID() {}

    public NewsID(String title, String author) {
        this.title = title;
        this.author = author;
    }

    private String title;
    private String author;

    @Override
    public int hashCode() {
        return HashTool.compute(author, title);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("title", title)
                .add("author", author);
    }
}
