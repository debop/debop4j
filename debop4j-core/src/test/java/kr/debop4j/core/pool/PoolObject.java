package kr.debop4j.core.pool;

import lombok.Data;

import java.net.URI;

/**
 * kr.debop4j.core.pool.PoolObject
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 8. 오전 10:31
 */
@Data
public class PoolObject {

    public PoolObject(String name, Integer intValue, URI uriValue) {
        this.name = name;
        this.intValue = intValue;
        this.uriValue = uriValue;

        // 생성 시에 많은 시간이 소비되는 것이여야 PoolObject 로서 사용하기 좋다.
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) { }
    }

    private String name;
    private Integer intValue;
    private URI uriValue;

    private Boolean isActive;
}
