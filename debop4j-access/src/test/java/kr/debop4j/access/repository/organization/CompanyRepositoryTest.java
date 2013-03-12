package kr.debop4j.access.repository.organization;

import com.google.common.collect.Iterables;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.repository.RepositoryTestBase;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * kr.debop4j.access.repository.organization.CompanyRepositoryTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 12.
 */
@Slf4j
public class CompanyRepositoryTest extends RepositoryTestBase {

    @Getter(lazy = true)
    private final CompanyRepository repository = Springs.getBean(CompanyRepository.class);


    private Company createCompany() {
        Company company = new Company("KTH", "케이티하이텔");
        getRepository().saveOrUpdate(company);

        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        return company;
    }

    @Test
    public void createTest() {
        Company company = createCompany();

        Company loaded = getRepository().get(company.getId());
        Assert.assertEquals(company.getCode(), loaded.getCode());
        Assert.assertEquals(company.getName(), loaded.getName());
    }

    @Test
    public void findByCode() {
        Company company = createCompany();
        Company loaded = getRepository().findByCode(DefaultCompanyCode);
        Assert.assertNotNull(loaded);
        Assert.assertEquals(DefaultCompanyCode, loaded.getCode());
    }

    @Test
    public void getByName() {
        Company company = createCompany();
        Company loaded = Iterables.getFirst(getRepository().findByName("케이"), null);
        Assert.assertNotNull(loaded);
        Assert.assertTrue(StringTool.contains(loaded.getName(), "케이"));
    }
}
