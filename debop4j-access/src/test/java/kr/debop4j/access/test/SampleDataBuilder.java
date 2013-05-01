package kr.debop4j.access.test;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Random;

/**
 * 테스트 샘플용 데이터 빌드용 기본 틀래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 14 오전 10:35
 */
@Slf4j
public abstract class SampleDataBuilder {

    public static final Random rnd = new Random(new Date().getTime());

    public abstract void createSampleData();
}
