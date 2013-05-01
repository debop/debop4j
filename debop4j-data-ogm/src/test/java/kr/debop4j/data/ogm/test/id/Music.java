package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * kr.debop4j.data.ogm.test.id.Music
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 2:19
 */
@Entity
@Getter
@Setter
public class Music extends AnnotatedEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "music")
    @TableGenerator(name = "music",
                    table = "sequence",
                    pkColumnName = "key",
                    pkColumnValue = "music",
                    valueColumnName = "seed")
    private Long id;

    private String name;

    private String composer;
}
