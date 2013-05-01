package kr.debop4j.core.parallelism;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;

/**
 * pudding.pudding.commons.parallelism.forkjoin.FibonacciTask
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 28.
 */
@Slf4j
public class FibonacciTask extends RecursiveTask<Long> {

    private static final long serialVersionUID = -2895357520012193296L;

    private static final int THRESHOLD = 5;

    private FibonacciProblem problem;
    @Getter
    private long result;

    public FibonacciTask(FibonacciProblem problem) {
        this.problem = problem;
    }

    @Override
    protected Long compute() {
        if (problem.n < THRESHOLD) {
            result = problem.solve();
        } else {
            FibonacciTask worker1 = new FibonacciTask(new FibonacciProblem(problem.n - 1));
            FibonacciTask worker2 = new FibonacciTask(new FibonacciProblem(problem.n - 2));

            worker1.fork();
            result = worker2.compute() + worker1.join();
        }
        return result;
    }
}
