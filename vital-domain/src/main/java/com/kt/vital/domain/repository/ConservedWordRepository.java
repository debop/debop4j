package com.kt.vital.domain.repository;

import com.kt.vital.domain.model.ConservedWord;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * {@link ConservedWord} Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19.
 */
@Repository
@Slf4j
public class ConservedWordRepository extends HibernateRepository<ConservedWord> {
    public ConservedWordRepository() {
        super(ConservedWord.class);
    }
}
