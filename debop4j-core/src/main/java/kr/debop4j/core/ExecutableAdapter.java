package kr.debop4j.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.debop4j.core.ExecutableAdapter
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 16.
 */
@Slf4j
public class ExecutableAdapter implements Runnable {

    private final Executable executable;
    private Throwable error;
    @Getter
    private boolean done;

    public ExecutableAdapter(Executable executable) {
        Guard.shouldNotBeNull(executable, "executable");
        this.executable = executable;
    }

    public void reThrowAnyErrrors() {
        if (error == null)
            return;

        if (RuntimeException.class.isInstance(error)) {
            throw RuntimeException.class.cast(error);
        } else if (Error.class.isInstance(error)) {
            throw Error.class.cast(error);
        } else {
            throw new ExceptionWrapper(error);
        }
    }

    @Override
    public void run() {
        if (ExecutableAdapter.log.isDebugEnabled())
            ExecutableAdapter.log.debug("Executable 인스턴스 실행을 시작합니다...");

        done = false;
        error = null;
        try {
            executable.execute();
            if (ExecutableAdapter.log.isDebugEnabled())
                ExecutableAdapter.log.debug("Executable 인스턴스 실행을 완료했습니다!!!");
        } catch (Throwable t) {
            if (ExecutableAdapter.log.isDebugEnabled())
                ExecutableAdapter.log.debug("Executable 인스턴스 실행에 실패했습니다!!!", t);
            error = t;
        } finally {
            done = true;
        }
    }


    public static class ExceptionWrapper extends RuntimeException {

        public ExceptionWrapper(Throwable cause) {
            super(cause);
        }
    }
}
