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

package kr.debop4j.core.parallelism;

import com.google.common.collect.Lists;
import kr.debop4j.core.Action1;
import kr.debop4j.core.AutoStopwatch;
import kr.debop4j.core.Function1;
import kr.debop4j.core.collection.NumberRange;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 11
 */
@Slf4j
public class ParallelsTest {

    private static final int LowerBound = 0;
    private static final int UpperBound = 99999;

    @Test
    public void parallelRunnable() {
        final Runnable runnable =
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = LowerBound; i < UpperBound; i++) {
                            Hero.findRoot(i);
                        }
                        if (log.isDebugEnabled())
                            log.debug("FindRoot({}) returns [{}]", UpperBound, Hero.findRoot(UpperBound));
                    }
                };

        @Cleanup
        AutoStopwatch stopwatch = new AutoStopwatch();
        Parallels.run(0, 100, runnable);
    }

    @Test
    public void parallelRunAction() {
        final Action1<Integer> action1 =
                new Action1<Integer>() {
                    @Override
                    public void perform(Integer x) {
                        for (int i = LowerBound; i < UpperBound; i++) {
                            Hero.findRoot(i);
                        }
                        if (log.isDebugEnabled())
                            log.debug("FindRoot({}) returns [{}]", UpperBound, Hero.findRoot(UpperBound));
                    }
                };

        @Cleanup
        AutoStopwatch stopwatch = new AutoStopwatch();
        Parallels.run(0, 100, action1);
    }

    @Test
    public void parallelRunEachAction() {
        final Action1<Integer> action1 =
                new Action1<Integer>() {
                    @Override
                    public void perform(Integer x) {
                        for (int i = LowerBound; i < UpperBound; i++) {
                            Hero.findRoot(i);
                        }
                        if (log.isDebugEnabled())
                            log.debug("FindRoot({}) returns [{}]", UpperBound, Hero.findRoot(UpperBound));
                    }
                };

        @Cleanup
        AutoStopwatch stopwatch = new AutoStopwatch();
        Parallels.runEach(NumberRange.range(0, 100), action1);
    }

    @Test
    public void parallelCallable() {
        final Callable<Double> callable =
                new Callable<Double>() {
                    @Override
                    public Double call() {
                        for (int i = LowerBound; i < UpperBound; i++) {
                            Hero.findRoot(i);
                        }
                        if (log.isDebugEnabled())
                            log.debug("FindRoot({}) returns [{}]", UpperBound, Hero.findRoot(UpperBound));
                        return Hero.findRoot(UpperBound);
                    }
                };

        @Cleanup
        AutoStopwatch stopwatch = new AutoStopwatch();
        List<Double> results = Parallels.run(0, 100, callable);

        Assert.assertNotNull(results);
        Assert.assertEquals(100, results.size());
    }

    @Test
    public void parallelRunFunction() {
        final Function1<Integer, Double> function1 =
                new Function1<Integer, Double>() {
                    @Override
                    public Double execute(Integer x) {
                        for (int i = LowerBound; i < UpperBound; i++) {
                            Hero.findRoot(i);
                        }
                        if (log.isDebugEnabled())
                            log.debug("FindRoot({}) returns [{}]", UpperBound, Hero.findRoot(UpperBound));
                        return Hero.findRoot(UpperBound);
                    }
                };

        @Cleanup
        AutoStopwatch stopwatch = new AutoStopwatch();
        List<Double> results = Parallels.run(0, 100, function1);

        Assert.assertNotNull(results);
        Assert.assertEquals(100, results.size());
    }

    @Test
    public void parallelRunEachFunction() {
        final Function1<Integer, Double> function1 =
                new Function1<Integer, Double>() {
                    @Override
                    public Double execute(Integer x) {
                        for (int i = LowerBound; i < UpperBound; i++) {
                            Hero.findRoot(i);
                        }
                        if (log.isDebugEnabled())
                            log.debug("FindRoot({}) returns [{}]", UpperBound, Hero.findRoot(UpperBound));
                        return Hero.findRoot(UpperBound);
                    }
                };

        @Cleanup
        AutoStopwatch stopwatch = new AutoStopwatch();
        List<Double> results = Parallels.runEach(NumberRange.range(0, 100), function1);
        Assert.assertNotNull(results);
        Assert.assertEquals(100, results.size());
    }

    @Test
    public void runPartitionsAction() throws Exception {
        final Action1<List<Integer>> action1 =
                new Action1<List<Integer>>() {
                    @Override
                    public void perform(List<Integer> xs) {
                        for (int x : xs) {
                            for (int i = LowerBound; i < UpperBound; i++) {
                                Hero.findRoot(i);
                            }
                            if (log.isDebugEnabled())
                                log.debug("FindRoot({}) returns [{}]", UpperBound, Hero.findRoot(UpperBound));
                        }
                    }
                };

        @Cleanup
        AutoStopwatch stopwatch = new AutoStopwatch();
        Parallels.runPartitions(NumberRange.range(0, 100), action1);
    }

    @Test
    public void runPartitionsFunction() {
        final Function1<List<Integer>, List<Double>> function1 =
                new Function1<List<Integer>, List<Double>>() {
                    @Override
                    public List<Double> execute(List<Integer> xs) {
                        List<Double> results = Lists.newArrayListWithCapacity(xs.size());
                        for (int x : xs) {
                            for (int i = LowerBound; i < UpperBound; i++) {
                                Hero.findRoot(i);
                            }
                            if (log.isDebugEnabled())
                                log.debug("FindRoot({}) returns [{}]", UpperBound, Hero.findRoot(UpperBound));

                            results.add(Hero.findRoot(UpperBound));
                        }
                        return results;
                    }
                };

        @Cleanup
        AutoStopwatch stopwatch = new AutoStopwatch();
        List<Double> results = Parallels.runPartitions(NumberRange.range(0, 100), function1);
        Assert.assertNotNull(results);
        Assert.assertEquals(100, results.size());
    }

    public static class Hero {

        private static final double Tolerance = 1.0e-10;

        public static double findRoot(double number) {
            double guess = 1.0;
            double error = Math.abs(guess * guess - number);

            while (error > Tolerance) {
                guess = (number / guess + guess) / 2.0;
                error = Math.abs(guess * guess - number);
            }
            return guess;
        }
    }
}
