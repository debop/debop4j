package kr.debop4j.core.unitTesting;

import kr.debop4j.core.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * kr.debop4j.core.unitTesting.TestToolTest
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 3.
 */
@Slf4j
public class TestToolTest extends AbstractTest {

    private static final int LowerBound = 0;
    private static final int UpperBound = 99999;

    @Test
    public void runTasksWithAction() {

        final Runnable runnable =
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = LowerBound; i < UpperBound; i++) {
                            Hero.findRoot(i);
                        }
                    }
                };

        TestTool.runTasks(100, new Runnable() {
            @Override
            public void run() {
                runnable.run();
                runnable.run();
            }
        });
    }

    @Test
    public void runTasksWithCallables() {

        final Callable<Double> callable = new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                for (int i = LowerBound; i < UpperBound; i++) {
                    Hero.findRoot(i);
                }
                return Hero.findRoot(UpperBound);
            }
        };

        TestTool.runTasks(100, new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return (callable.call() + callable.call()) / 2.0;
            }
        });
    }

    public static class Hero {

        private static final double Tolerance = 1.0e-8;

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
