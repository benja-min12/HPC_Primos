import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class parallel {
    private static final long N = 1000000;

    private static void executeInThreads(int threads) throws InterruptedException {
        final ExecutorService threadPool = Executors.newFixedThreadPool(threads);
        long start = System.currentTimeMillis();
        final AtomicLong sum = new AtomicLong(0);
        int workPerThread = (int) (N / threads);
        final int[] startI = new int[threads];
        final int[] endI = new int[threads];
        for (int i = 0; i < threads; i++) {
            startI[i] = i * workPerThread;
            endI[i] = (i + 1) * workPerThread;
        }
        startI[0] = 1;
        for (int i = 1; i <= threads; i++) {

            int finalI = i-1;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    for (int j = startI[finalI]; j < endI[finalI]; j++) {
                        if (isPrime(j)) {
                            sum.incrementAndGet();
                        }
                    }


                }

            });

        }


        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("Time: " + (System.currentTimeMillis()- start) + " ms");
        System.out.println("Threads: " + threads + " Primes: " +  sum.get());

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
        executeInThreads(12);
    }


}
