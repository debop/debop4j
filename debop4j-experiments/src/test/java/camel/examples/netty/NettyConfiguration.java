package camel.examples.netty;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * camel.examples.netty.NettyConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 30. 오후 8:11
 */
public class NettyConfiguration {

    @Test
    public void buildRouteBuilder() {
        RouteBuilder builder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("netty:tcp://localhost:9090")
                        .to("mock:result");
            }
        };
        Assert.assertNotNull(builder);
    }

}
