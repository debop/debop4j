package kr.debop4j.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;

/**
 * 단위 테스트 클래스의 최상위 추상 클래스
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12
 */
@Slf4j
public abstract class AbstractTest {

//    @Rule
//    public MethodRule benchmarkRun = new BenchmarkRule();

    @Before
    public void before() {
        onBefore();
    }

    @After
    public void after() {
        onAfter();
    }

    protected void onBefore() { }

    protected void onAfter() { }
}
