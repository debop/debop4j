package kr.debop4j.web.async;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 비동기 방식으로 작업을 수행하는 Servlet의 기본 클래스입니다.
 * <b />
 * 서블릿에서 여러 작업이 동시에 이루어지던가, 네트웍이나 파일 작업이 주로 하는 경우 비동기 방식을 사용하면 확장성에 좋다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 19
 */
@Slf4j
@WebServlet(asyncSupported = true)
public abstract class AsyncServletBase extends HttpServlet {

    private static final long serialVersionUID = 3628741189209483408L;

    @Getter
    @Setter
    private AsyncServletRunnable asyncDoGet;

    @Getter
    @Setter AsyncServletRunnable asyncDoPost;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final AsyncServletRunnable getHandler = getAsyncDoGet();
        if (getHandler == null)
            return;

        if (log.isDebugEnabled())
            log.debug("비동기 방식으로 get method를 처리합니다...");

        final AsyncContext asyncCtx = req.startAsync(req, resp);
        AsyncServletRunnable asyncRun = new AsyncServletRunnable() {
            @Override
            public void run() {
                try {
                    getHandler.setAsyncContext(asyncCtx);
                    getHandler.run();
                } catch (Exception e) {
                    log.error("비동기 방식으로 GET 메소드를 처리하는 동안 예외가 발생했습니다.", e);
                    throw new RuntimeException(e);
                } finally {
                    asyncCtx.complete();
                }
            }
        };

        if (log.isDebugEnabled())
            log.debug("비동기 방식으로 GET method 처리를 시작했습니다...");

        asyncCtx.start(asyncRun);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final AsyncServletRunnable postHandler = getAsyncDoPost();
        if (postHandler == null)
            return;

        if (log.isDebugEnabled())
            log.debug("비동기 방식으로 POST method를 처리합니다...");

        final AsyncContext asyncCtx = req.startAsync(req, resp);
        AsyncServletRunnable asyncRun = new AsyncServletRunnable() {
            @Override
            public void run() {
                try {
                    postHandler.setAsyncContext(asyncCtx);
                    postHandler.run();
                } catch (Exception e) {
                    log.error("비동기 방식으로 POST 메소드를 처리하는 동안 예외가 발생했습니다.", e);
                    throw new RuntimeException(e);
                } finally {
                    asyncCtx.complete();
                }
            }
        };

        if (log.isDebugEnabled())
            log.debug("비동기 방식으로 POST method 처리를 시작했습니다...");

        asyncCtx.start(asyncRun);
    }
}
