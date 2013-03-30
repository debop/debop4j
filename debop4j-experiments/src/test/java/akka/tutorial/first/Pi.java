package akka.tutorial.first;

import akka.actor.*;
import akka.routing.RoundRobinRouter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * akka.tutorial.first.Pi
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 30. 오후 2:57
 */
@Slf4j
public class Pi {

    public static void main(String[] args) {
        Pi pi = new Pi();
        pi.calculate(4, 10000, 10000);
    }

    public void calculate(final int nrOfWorkers, final int nrOfElements, final int nrOfMessages) {
        ActorSystem system = ActorSystem.create("PiSystem");

        final ActorRef listener = system.actorOf(new Props(Listener.class), "listener");

        // create the master
        ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {

            private static final long serialVersionUID = 3219900776631104378L;

            /**
             * This method must return a different instance upon every call.
             */
            @Override
            public Actor create() throws Exception {
                return new Master(nrOfWorkers, nrOfMessages, nrOfElements, listener);
            }
        }), "master");

        master.tell(new Calculate());
    }

    static class Calculate {}

    static class Work {
        @Getter
        private final int start;
        @Getter
        private final int nrOfElements;

        public Work(int start, int nrOfElements) {
            this.start = start;
            this.nrOfElements = nrOfElements;
        }
    }

    static class Result {
        @Getter
        private final double value;

        public Result(double value) {
            this.value = value;
        }
    }

    static class PiApproximation {
        @Getter
        private final double pi;
        @Getter
        private final Duration duration;

        public PiApproximation(double pi, Duration duration) {
            this.pi = pi;
            this.duration = duration;
        }
    }


    public static class Worker extends UntypedActor {

        private double calculatePiFor(int start, int nrOfElements) {
            double acc = 0.0;
            for (int i = start * nrOfElements; i <= (start + 1) * nrOfElements - 1; i++) {
                acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
            }
            return acc;
        }

        /**
         * To be implemented by concrete UntypedActor, this defines the behavior of the
         * UntypedActor.
         */
        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof Work) {
                Work work = (Work) message;
                double result = calculatePiFor(work.getStart(), work.getNrOfElements());
                getSender().tell(new Result(result), getSelf());
            } else {
                unhandled(message);
            }
        }
    }

    public static class Master extends UntypedActor {
        private final int nrOfMessages;
        private final int nrOfElements;

        private double pi;
        private int nrOfResults;
        private final long start = System.currentTimeMillis();

        private final ActorRef listener;
        private final ActorRef workerRouter;

        public Master(final int nrOfWorkers, int nrOfMessages, int nrOfElements, ActorRef listener) {
            this.nrOfMessages = nrOfMessages;
            this.nrOfElements = nrOfElements;
            this.listener = listener;

            workerRouter = this.getContext().actorOf(new Props(Worker.class)
                                                             .withRouter(new RoundRobinRouter(nrOfWorkers)),
                                                     "workerRouter");
        }

        public void onReceive(Object message) {
            if (message instanceof Calculate) {
                for (int start = 0; start < nrOfMessages; start++) {
                    workerRouter.tell(new Work(start, nrOfElements), getSelf());
                }
            } else if (message instanceof Result) {
                Result result = (Result) message;
                pi += result.getValue();
                nrOfResults += 1;
                if (nrOfResults == nrOfMessages) {
                    Duration duration = Duration.create(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
                    listener.tell(new PiApproximation(pi, duration), getSelf());
                    getContext().stop(getSelf());
                }
            } else {
                unhandled(message);
            }
        }
    }

    public static class Listener extends UntypedActor {
        /**
         * To be implemented by concrete UntypedActor, this defines the behavior of the
         * UntypedActor.
         */
        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof PiApproximation) {
                PiApproximation approximation = (PiApproximation) message;
                log.info("Pi approximation:[{}], Calculation time=[{}]",
                         approximation.getPi(), approximation.getDuration());
                getContext().system().shutdown();
            } else {
                unhandled(message);
            }
        }
    }
}
