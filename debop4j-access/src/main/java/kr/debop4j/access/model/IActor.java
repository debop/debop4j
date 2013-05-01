package kr.debop4j.access.model;

/**
 * 행위자를 나타냅니다. (회사, 조직, 그룹, 직원, 사용자)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 17.
 */
public interface IActor extends ICodeBaseEntity {

    Long getId();

    String getName();
}
