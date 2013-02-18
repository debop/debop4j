package kr.debop4j.core;

import lombok.Getter;

/**
 * kr.nsoft.commons.AutoCloseableAction
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 30.
 */
public class AutoCloseableAction implements AutoCloseable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AutoCloseableAction.class);

    @Getter
    private final Runnable action;
    @Getter
    protected boolean closed;

    public AutoCloseableAction(final Runnable action) {
        Guard.shouldNotBeNull(action, "action");
        this.action = action;
        this.closed = false;
    }

    @Override
    public void close() {
        if (closed)
            return;

        try {
            if (log.isDebugEnabled())
                log.debug("AutoCloseable의 close 작업을 수행합니다...");

            if (action != null)
                action.run();

            if (log.isDebugEnabled())
                log.debug("AutoCloseable의 close 작업을 완료했습니다.");

        } catch (Exception e) {
            if (log.isWarnEnabled())
                log.warn("AutoClosesable의 close 작업에 예외가 발생했습니다. 예외는 무시됩니다.", e);
        } finally {
            closed = true;
        }
    }
}
