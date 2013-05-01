package kr.debop4j.access.model.product;

/**
 * kr.debop4j.access.model.product.AuthorityKind
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 11.
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
