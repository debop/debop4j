package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * kr.debop4j.data.ogm.test.id.Video
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 2:28
 */
@Entity
@Getter
@Setter
public class Video extends AnnotatedEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "video")
    @TableGenerator(name = "video",
                    table = "sequences",
                    pkColumnName = "key",
                    pkColumnValue = "video",
                    valueColumnName = "seed")
    private Integer id;

    public String name;

    private String director;
}
