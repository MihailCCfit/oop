package nsu.fit.tsukanov.parallel.prime.implementations.hybrid;

import nsu.fit.tsukanov.parallel.prime.core.CheckerProvider;
import nsu.fit.tsukanov.parallel.prime.core.NonPrimesFinder;
import nsu.fit.tsukanov.parallel.prime.core.PrimeNumberChecker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Hybrid implements NonPrimesFinder {

    private static class MyBoolean {
        boolean flag;

        public MyBoolean() {
            this(false);
        }

        public MyBoolean(boolean flag) {
            this.flag = flag;
        }
    }

    /**
     * Check collection for containing prime numbers.
     *
     * @param integers collection of numbers for checking
     * @return true if collection has a complex number
     */
    @Override
    public boolean hasNoPrime(Collection<Integer> integers) {
        int[] arr = new int[]{10, 100, 500, 2500, 5000};
        int[] threadTable = new int[]{1, 2, 4, 6, 12};
        int j = 0;
        int size = integers.size();
        while (j < arr.length - 1 && size > arr[j]) {
            j++;
        }
        int numberOfThreads = threadTable[j];
        if (numberOfThreads == 1) {
            PrimeNumberChecker primeNumberChecker = CheckerProvider.create(integers);
            for (Integer integer : integers) {
                if (primeNumberChecker.notPrime(integer)) {
                    return true;
                }
            }
            return false;
        }
        WorkingThread workingThread = new WorkingThread(integers,
                CheckerProvider.create(integers, numberOfThreads));
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(workingThread));
            threads.get(i).start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return workingThread.result.flag;
    }

    private static class WorkingThread implements Runnable {
        private final Iterator<Integer> iterator;
        private final MyBoolean result = new MyBoolean();
        private final PrimeNumberChecker primeNumberChecker;

        private WorkingThread(Collection<Integer> integers, PrimeNumberChecker primeNumberChecker) {
            this.primeNumberChecker = primeNumberChecker;
            iterator = integers.iterator();
        }

        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            Integer number;
            while ((number = getNext()) != null) {
                if (primeNumberChecker.notPrime(number)) {
                    result.flag = true;
                }
            }
        }

        private Integer getNext() {
            if (result.flag) {
                return null;
            }
            synchronized (iterator) {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            }
            return null;
        }
    }
}
