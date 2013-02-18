package kr.debop4j.core.cryptography.symmetric;

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;

/**
 * 설명을 추가하세요.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 18
 */
public abstract class SymmetricByteEncryptorBase implements ISymmetricByteEncryptor {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SymmetricByteEncryptorBase.class);

    private final StandardPBEByteEncryptor byteEncryptor;

    public SymmetricByteEncryptorBase() {
        this("sunghyouk.bae@gmail.com");
    }

    public SymmetricByteEncryptorBase(String password) {
        byteEncryptor = new StandardPBEByteEncryptor();
        byteEncryptor.setAlgorithm(getAlgorithm());
        byteEncryptor.setPassword(password);

        if (log.isDebugEnabled())
            log.debug("[{}] 인스턴스를 생성했습니다. algorithm=[{}]", getClass().getName(), getAlgorithm());
    }

    abstract public String getAlgorithm();

    @Override
    public boolean isInitialized() {
        return byteEncryptor.isInitialized();
    }

    @Override
    public void setPassword(String password) {
        byteEncryptor.setPassword(password);
    }

    @Override
    public byte[] encrypt(byte[] plainBytes) {
        if (plainBytes == null || plainBytes.length == 0)
            return new byte[0];

        return byteEncryptor.encrypt(plainBytes);
    }

    @Override
    public byte[] decrypt(byte[] encryptedBytes) {
        if (encryptedBytes == null || encryptedBytes.length == 0)
            return new byte[0];

        return byteEncryptor.decrypt(encryptedBytes);
    }
}
