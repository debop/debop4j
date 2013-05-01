package kr.debop4j.access.test.model.organization;

import kr.debop4j.access.AccessContext;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.test.SampleData;
import kr.debop4j.access.test.SampleDataBuilder;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * kr.debop4j.access.test.model.organization.OrganizationSampleDataBuilder
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 14 오전 10:38
 */
@Slf4j
public class OrganizationSampleDataBuilder extends SampleDataBuilder {

    @Override
    public void createSampleData() {
        createCompany();
        UnitOfWorks.getCurrent().transactionalFlush();
    }


    private void createCompany() {
        Company company = new Company(AccessContext.Current.getCompanyCode());
        company.setName(company.getCode() + " Name");
        company.setDescription("테스트용 기본 클래스");
        company.setExAttr("확장 속성 정보입니다.");

        company.addLocaleValue(Locale.ENGLISH, new Company.CompanyLocale("DefaultCompany", "Default Company for Testing"));
        company.addLocaleValue(Locale.KOREAN, new Company.CompanyLocale("기본 회사", "테스트를 위한 기본 회사"));

        UnitOfWorks.getCurrentSession().saveOrUpdate(company);

        for (String code : SampleData.getCompanyCodes()) {
            company = new Company(code);
            company.setName(company.getCode() + " Name");
            company.setDescription("테스트용 기본 클래스");
            company.setExAttr("확장 속성 정보입니다.");
            company.setActive(true);
            UnitOfWorks.getCurrentSession().saveOrUpdate(company);
        }
    }
}
