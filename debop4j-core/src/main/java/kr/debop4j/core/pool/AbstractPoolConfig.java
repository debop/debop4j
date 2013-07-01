/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.core.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * Apache common pool 을 설정 정보
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 8. 오전 10:53
 */
@Slf4j
public abstract class AbstractPoolConfig extends GenericObjectPool.Config {

    /**
     * Gets max idle.
     *
     * @return the max idle
     */
    public int getMaxIdle() {
        return maxIdle;
    }

    /**
     * Sets max idle.
     *
     * @param maxIdle the max idle
     */
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    /**
     * Gets min idle.
     *
     * @return the min idle
     */
    public int getMinIdle() {
        return minIdle;
    }

    /**
     * Sets min idle.
     *
     * @param minIdle the min idle
     */
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * Gets max active.
     *
     * @return the max active
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * Sets max active.
     *
     * @param maxActive the max active
     */
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * Gets max wait.
     *
     * @return the max wait
     */
    public long getMaxWait() {
        return maxWait;
    }

    /**
     * Sets max wait.
     *
     * @param maxWait the max wait
     */
    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * Gets when exhausted action.
     *
     * @return the when exhausted action
     */
    public byte getWhenExhaustedAction() {
        return whenExhaustedAction;
    }

    /**
     * Sets when exhausted action.
     *
     * @param whenExhaustedAction the when exhausted action
     */
    public void setWhenExhaustedAction(byte whenExhaustedAction) {
        this.whenExhaustedAction = whenExhaustedAction;
    }

    /**
     * Is test on borrow.
     *
     * @return the boolean
     */
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    /**
     * Sets test on borrow.
     *
     * @param testOnBorrow the test on borrow
     */
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    /**
     * Is test on return.
     *
     * @return the boolean
     */
    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    /**
     * Sets test on return.
     *
     * @param testOnReturn the test on return
     */
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    /**
     * Is test while idle.
     *
     * @return the boolean
     */
    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    /**
     * Sets test while idle.
     *
     * @param testWhileIdle the test while idle
     */
    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    /**
     * Gets time between eviction runs millis.
     *
     * @return the time between eviction runs millis
     */
    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    /**
     * Sets time between eviction runs millis.
     *
     * @param timeBetweenEvictionRunsMillis the time between eviction runs millis
     */
    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    /**
     * Gets num tests per eviction run.
     *
     * @return the num tests per eviction run
     */
    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    /**
     * Sets num tests per eviction run.
     *
     * @param numTestsPerEvictionRun the num tests per eviction run
     */
    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    /**
     * Gets min evictable idle time millis.
     *
     * @return the min evictable idle time millis
     */
    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    /**
     * Sets min evictable idle time millis.
     *
     * @param minEvictableIdleTimeMillis the min evictable idle time millis
     */
    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    /**
     * Gets soft min evictable idle time millis.
     *
     * @return the soft min evictable idle time millis
     */
    public long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    /**
     * Sets soft min evictable idle time millis.
     *
     * @param softMinEvictableIdleTimeMillis the soft min evictable idle time millis
     */
    public void setSoftMinEvictableIdleTimeMillis(
            long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }
}
