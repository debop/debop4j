package kr.debop4j.data.ogm.test.type;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * kr.debop4j.data.ogm.test.type.Bookmark
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 5:16
 */
@Entity
@Getter
@Setter
public class Bookmark extends AnnotatedEntityBase {

    @Id
    private String id;

    private String description;

    private URL url;

    @Column(name = "site_weight")
    private BigDecimal siteWeight;

    @Column(name = "visits_count")
    private BigInteger visitCount;

    @Column(name = "is_favorite")
    private Boolean isFavorite;

    @Column(name = "display_mask")
    private Byte displayMask;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Temporal(TemporalType.TIME)
    private Date updateDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date destructionDate;

    @Temporal(TemporalType.DATE)
    private Calendar creationCalendar;

    // not supported
//    @Temporal(TemporalType.TIME)
//    private Calendar updateCalendar;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar destructionCalendar;

    @Enumerated(EnumType.STRING)
    public BookmarkType type;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "DS_BLOB")
    private byte[] blob;

    private UUID serialNumber;

    private Integer stockCount;

    private Long userId;

//    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
//    private DateTime jodaTime;
}
