package kr.debop4j.access.repository.organization;

import kr.debop4j.access.model.organization.CompanyCode;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

/**
 * {@link CompanyCode}에 대한 Repository 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 12.
 */
@Repository
@Slf4j
public class CodeRepository extends HibernateRepository<CompanyCode> {

    public CodeRepository() {
        super(CompanyCode.class);
    }

    public DetachedCriteria buildCriteria(String code, String name, Boolean active) {
        DetachedCriteria dc = DetachedCriteria.forClass(CompanyCode.class);

        if (StringTool.isNotEmpty(code))
            CriteriaTool.addEq(dc, "code", code);
        if (StringTool.isNotEmpty(name))
            CriteriaTool.addEq(dc, "name", name);
        if (active != null)
            CriteriaTool.addEq(dc, "active", active);

        return dc;
    }
}
