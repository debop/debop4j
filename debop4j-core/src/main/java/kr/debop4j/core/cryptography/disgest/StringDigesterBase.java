package kr.debop4j.core.cryptography.disgest;

import org.jasypt.digest.StandardStringDigester;

/**
 * 설명을 추가하세요.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 18
 */
public abstract class StringDigesterBase implements IStringDigester {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StringDigesterBase.class);

    private final StandardStringDigester standardStringDigester;

    public StringDigesterBase() {
        this(5);
    }

    public StringDigesterBase(int iterations) {
        standardStringDigester = new StandardStringDigester();
        standardStringDigester.setAlgorithm(getAlgorithm());
        standardStringDigester.setIterations(iterations);

        if (log.isDebugEnabled())
            log.debug("문자열을 암호화하는 [{}] 인스턴스를 생성했습니다. algorithm=[{}], iteration=[{}]",
                      getClass().getName(), getAlgorithm(), iterations);
    }

    abstract public String getAlgorithm();

    @Override
    public boolean isInitialized() {
        return standardStringDigester.isInitialized();
    }

    @Override
    public String digest(String message) {
        return standardStringDigester.digest(message);
    }

    @Override
    public boolean matches(String message, String digest) {
        return standardStringDigester.matches(message, digest);
    }
}
