import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class parallel {
    private static final long N = 50;

    private static void executeInThreads(int threads) throws InterruptedException {
        final ExecutorService threadPool = Executors.newFixedThreadPool(threads);
        long start = System.currentTimeMillis();
        for (long i = 1; i <= N; i++) {
            threadPool.execute(new ParallelTask(i));
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("Time: " + (System.currentTimeMillis()- start) + " ms");
        System.out.println("Threads: " + threads + " Primes: " +  ParallelTask.getPrimes());
        ParallelTask.resetPrimes();
    }
    private static class ParallelTask implements Runnable {


        private static final AtomicLong countPrimes = new AtomicLong();


        private final long number;


        public ParallelTask(long number) {
            this.number = number;
        }


        public static long getPrimes() {
            return countPrimes.get();
        }


        public static void resetPrimes() {
            countPrimes.set(0);
        }


        @Override
        public void run() {

            if (isPrime(this.number)) {
                countPrimes.getAndIncrement();
            }

        }

    }


    public static boolean isPrime(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Error in n: Can't process negative numbers");
        }
        if (n == 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        long max = (long) Math.sqrt(n);
        for (long i = 3; i <= max; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        executeInThreads(3);
    }


}
