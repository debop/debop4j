package vertx.examples;

import lombok.extern.slf4j.Slf4j;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.deploy.Verticle;

/**
 * vertx.examples.HelloServer
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 30. 오후 10:55
 */
@Slf4j
public class HelloServer extends Verticle {
    @Override
    public void start() throws Exception {
        Vertx.newVertx().createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest req) {
                log.debug("Got request=[{}]", req.uri);
                log.debug("Headers are: ");
                for (String key : req.headers().keySet()) {
                    log.debug(key + ":" + req.headers().get(key));
                }

                req.response.headers().put("Content-Type", "text/html; charset=UTF-8");
                req.response.end("<html><body><h1>Hello from vert.x!</h1></body></html>");
            }
        }).listen(9090);
        log.debug("Start listen 9090");
    }

    public static void main(String[] args) {
        HelloServer server = new HelloServer();
        try {
            log.debug("Start server...");
            server.start();
            System.in.read();

            log.debug("Stop server...");
            server.stop();
        } catch (Exception e) {
            log.error("예외 발생", e);
        }
    }
}
