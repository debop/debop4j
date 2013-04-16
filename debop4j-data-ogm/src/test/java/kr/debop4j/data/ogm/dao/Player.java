package kr.debop4j.data.ogm.dao;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;

/**
 * kr.debop4j.data.ogm.dao.Player
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오전 11:34
 */
@Entity
@Table(name = "player")
@Indexed
@Getter
@Setter
public class Player extends UuidEntityBase {

    private static final long serialVersionUID = 7317574732346075920L;

    @Column(name = "player_name")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
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

}
