package kr.debop4j.data.couchbase;

import com.couchbase.client.CouchbaseClient;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.internal.OperationFuture;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Couchbase 테스트
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 6:15
 */
@Slf4j
public class CouchbaseClientTest {

    private CouchbaseClient client;

    @Before
    public void before() throws Exception {
        List<URI> servers = new ArrayList<URI>();
        servers.add(URI.create("http://localhost:8091/pools"));

        client = new CouchbaseClient(servers, "default", "");
    }

    @Test
    public void connectCouchbase() throws Exception {
        OperationFuture<Boolean> setOp = client.set("abc", 10, "value");
        assertThat(setOp.getStatus().isSuccess()).isTrue();
    }
}
