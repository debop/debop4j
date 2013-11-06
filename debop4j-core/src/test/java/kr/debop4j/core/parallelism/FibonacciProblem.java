package kr.debop4j.core.parallelism;

import lombok.extern.slf4j.Slf4j;

/**
 * pudding.pudding.commons.parallelism.forkjoin.FibonacciProblem
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 28.
 */
@Slf4j
public class FibonacciProblem {

    public int n;

    public FibonacciProblem(int n) {
        this.n = n;
    }

    public long solve() {
        return fibonacci(n);
    }

    private long fibonacci(int n) {

            log.trace("Fibonacci calculates... n=[{}]", n);

        if (n <= 1)
            return n;
        else
            return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
