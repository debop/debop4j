package kr.debop4j.data.mapping.model.annotated.collection;

import com.google.common.collect.Maps;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Map;

/**
 * org.annotated.mapping.domain.model.collection.Car
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 15.
 */
@Entity
@Table(name = "`JPA_Car`")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Car extends AnnotatedEntityBase {

    private static final long serialVersionUID = 1714924676723258918L;

    @Id
    @GeneratedValue
    @Column(name = "CarId")
    private Long id;

    @CollectionTable(name = "CarOptionMap", joinColumns = @JoinColumn(name = "CarId"))
    @MapKeyClass(String.class)
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Map<String, String> options = Maps.newHashMap();

    @CollectionTable(name = "CarOptionTable", joinColumns = @JoinColumn(name = "CarId"))
    @MapKeyClass(String.class)
    @ElementCollection(targetClass = CarOption.class, fetch = FetchType.EAGER)
    private Map<String, CarOption> carOptions = Maps.newHashMap();
}
