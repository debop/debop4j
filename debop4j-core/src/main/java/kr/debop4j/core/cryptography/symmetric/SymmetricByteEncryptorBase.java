package kr.debop4j.core.cryptography.symmetric;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;

/**
 * 대칭형 암호화 알고리즘을 수행하는 기본 클래스입니다. 암호화/복호화를 수행합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 18
 */
@Slf4j
public abstract class SymmetricByteEncryptorBase implements ISymmetricByteEncryptor {

    static final String DEFAULT_PASSWORD = "sunghyouk.bae@gmail.com";
    private final StandardPBEByteEncryptor byteEncryptor;

    public SymmetricByteEncryptorBase() {
        this(DEFAULT_PASSWORD);
    }

    public SymmetricByteEncryptorBase(String password) {
        byteEncryptor = new StandardPBEByteEncryptor();
        byteEncryptor.setAlgorithm(getAlgorithm());
        byteEncryptor.setPassword(password);

        if (log.isDebugEnabled())
            log.debug("[{}] 인스턴스를 생성했습니다. algorithm=[{}]", getClass().getName(), getAlgorithm());
    }

    abstract public String getAlgorithm();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitialized() {
        return byteEncryptor.isInitialized();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(String password) {
        byteEncryptor.setPassword(password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] encrypt(byte[] plainBytes) {
        if (plainBytes == null || plainBytes.length == 0)
            return new byte[0];

        if (log.isTraceEnabled())
            log.trace("바이트 배열을 암호화합니다...");

        return byteEncryptor.encrypt(plainBytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] decrypt(byte[] encryptedBytes) {
        if (encryptedBytes == null || encryptedBytes.length == 0)
            return new byte[0];

        if (log.isTraceEnabled())
            log.trace("바이트 배열을 복호화합니다...");

        return byteEncryptor.decrypt(encryptedBytes);
    }
}
