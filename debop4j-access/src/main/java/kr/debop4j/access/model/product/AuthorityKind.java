package kr.debop4j.access.model.product;

/**
 * kr.debop4j.access.model.product.AuthorityKind
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
public enum AuthorityKind {

    None("None"),
    Create("Create"),
    Read("Read"),
    Update("Update"),
    Delete("Delete"),
    All("All");

    private String authorityKind;

    AuthorityKind(String authorityKind) {
        this.authorityKind = authorityKind;
    }
}
