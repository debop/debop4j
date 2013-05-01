package kr.debop4j.access.model.organization;

/**
 * 조직의 종류 (회사, 부서, 그룹, 직원)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 5 오후 4:27
 */
public enum OrganizationKind {

    Company("Company"),
    Department("Department"),
    Employee("Employee"),
    Group("Group");

    private final String organizationKind;

    OrganizationKind(String organizationKind) {
        this.organizationKind = organizationKind;
    }
}
