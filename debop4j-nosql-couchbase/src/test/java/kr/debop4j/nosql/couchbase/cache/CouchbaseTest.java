package kr.debop4j.nosql.couchbase.cache;

import com.couchbase.client.CouchbaseClient;
import com.google.common.collect.Lists;
import net.spy.memcached.internal.OperationFuture;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.util.List;

/**
 * kr.debop4j.core.cache.couchbase.CouchbaseTest
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 25. 오후 7:53
 */
public class CouchbaseTest {

    @Test
    public void connectCouchbase() throws Exception {
        List<URI> uris = Lists.newLinkedList();
        uris.add(URI.create("http://localhost:8091/pools"));

        CouchbaseClient client = new CouchbaseClient(uris, "default", "");

        OperationFuture<Boolean> setOp = client.set("abc", 0, "value");

        Assert.assertTrue(setOp.get().booleanValue());
    }
}
