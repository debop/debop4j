package kr.debop4j.access.model.product;

/**
 * kr.debop4j.access.model.product.ActorKind
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
public enum ActorKind {

    Company("Company"),
    Department("Department"),
    Employee("Employee"),
    Group("Group"),
    User("User");

    private final String actorKind;

    ActorKind(String actorKind) {
        this.actorKind = actorKind;
    }
}
