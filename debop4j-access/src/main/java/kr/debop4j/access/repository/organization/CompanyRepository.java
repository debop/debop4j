package kr.debop4j.access.repository.organization;

import kr.debop4j.access.model.organization.Company;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.repository.impl.HibernateRepository;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Company} 에 대한 Repository 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Repository
@Slf4j
public class CompanyRepository extends HibernateRepository<Company> {

    public CompanyRepository() {
        super(Company.class);
    }

    public DetachedCriteria buildCriteria(String code, String name, Boolean active) {

        DetachedCriteria dc = DetachedCriteria.forClass(Company.class);

        if (StringTool.isNotEmpty(code))
            dc = CriteriaTool.addEq(dc, "code", code);

        if (StringTool.isNotEmpty(name))
            dc = CriteriaTool.addEq(dc, "name", name);

        if (active != null)
            dc = CriteriaTool.addEq(dc, "active", active);

        return dc;
    }

    public Company findByCode(String code) {
        DetachedCriteria dc =
                DetachedCriteria.forClass(Company.class)
                        .add(Restrictions.eq("code", code));
        return findOne(dc);
    }

    public List<Company> findByName(String name) {
        DetachedCriteria dc = DetachedCriteria.forClass(Company.class);
        CriteriaTool.addILike(dc, "name", name);
        return find(dc);
    }

    public List<Company> findAllByActive(boolean active) {
        DetachedCriteria dc = DetachedCriteria.forClass(Company.class);
        return find(dc.add(Restrictions.eq("active", active)));
    }
}
