package kr.debop4j.web.examples.async;

import com.google.common.base.Charsets;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.util.EntityUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Future;

/**
 * examples.async.AsyncLoadPage
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 17.
 */
@Slf4j
@WebServlet(urlPatterns = "/async", asyncSupported = true)
public class AsyncLoadPage extends HttpServlet {

    private static final long serialVersionUID = 8839854702079067625L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext asyncContext = req.startAsync(req, resp);
        AsyncLoadResource asyncTask = new AsyncLoadResource(asyncContext, "http://daum.net");
        asyncContext.start(asyncTask);
    }

    public static class AsyncLoadResource implements Runnable {

        private final AsyncContext ctx;
        private final String url;

        public AsyncLoadResource(final AsyncContext ctx, final String url) {
            this.ctx = ctx;
            this.url = url;
        }

        @Override
        public void run() {
            // To change body of implemented methods use File | Settings | File Templates.if (log.isDebugEnabled())
            log.debug("URI=[{}] 의 웹 컨텐츠를 비동기 방식으로 다운로드 받아 캐시합니다.", url);

            try {

                String responseStr;
                HttpAsyncClient httpClient = new DefaultHttpAsyncClient();
                httpClient.start();
                HttpGet request = new HttpGet(url);
                Future<HttpResponse> future = httpClient.execute(request, null);

                HttpResponse response = future.get();
                responseStr = EntityUtils.toString(response.getEntity(), Charsets.UTF_8.toString());
                httpClient.shutdown();

                if (log.isDebugEnabled())
                    log.debug("URI=[{}]로부터 웹 컨텐츠를 다운로드 받았습니다. responseStr=[{}]",
                              url, StringTool.ellipsisChar(responseStr, 255));

                ctx.getResponse().setCharacterEncoding("UTF-8");
                PrintWriter writer = ctx.getResponse().getWriter();
                writer.write(responseStr);
                writer.close();
                ctx.complete();
            } catch (Exception ignored) {
                throw new RuntimeException(ignored);
            }
        }
    }
}


