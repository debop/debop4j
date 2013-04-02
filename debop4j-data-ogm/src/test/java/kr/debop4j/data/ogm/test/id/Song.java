package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * kr.debop4j.data.ogm.test.id.Song
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 2:26
 */
@Entity
@Getter
@Setter
public class Song extends AnnotatedEntityBase {

    static final transient int INITIAL_VALUE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "songSequenceGenerator")
    @SequenceGenerator(name = "songSequenceGenerator",
                       sequenceName = "song_sequence_name",
                       initialValue = INITIAL_VALUE,
                       allocationSize = 10)
    private Long id;

    private String title;

    private String singer;
}
