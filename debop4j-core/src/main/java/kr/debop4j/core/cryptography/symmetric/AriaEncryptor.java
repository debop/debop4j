package kr.debop4j.core.cryptography.symmetric;


/**
 * ARIA 알고리즘을 이용한 대칭형 암호화기<br/>
 * 참고 : <a href="http://seed.kisa.or.kr/kor/aria/aria.jsp">ARIA 알고리즘</a>
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 14
 */
public final class AriaEncryptor extends SymmetricByteEncryptorBase {

    @Override
    public final String getAlgorithm() {
        return "Aria";
    }

    @Override
    public boolean isInitialized() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPassword(String password) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public byte[] encrypt(byte[] plainBytes) {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public byte[] decrypt(byte[] encryptedBytes) {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

}
