package org.hibernate.redis.jedis;

import redis.clients.jedis.Pipeline;

/**
 * Pipelining 방식으로 작업하는 것
 * 참고: https://github.com/xetorthio/jedis/wiki/AdvancedUsage#pipelining
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 5. 오전 5:06
 */
public interface JedisPipelineCallback {

    void execute(Pipeline pipeline);
}
