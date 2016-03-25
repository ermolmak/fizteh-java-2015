package ru.fizteh.fivt.students.ermolmak.threads.blockingqueue;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tests {
    private List<Integer> arrayToList(Integer[] a) {
        List<Integer> result = new ArrayList<>();
        Collections.addAll(result, a);
        return result;
    }

    @Test
    public void test1() {
        Integer[] a = { 1, 2, 3, 4 };
        List<Integer> listA = arrayToList(a);
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(5);

        try {
            blockingQueue.offer(arrayToList(a));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Integer> listB = new ArrayList<>();
        try {
            listB = blockingQueue.take(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertArrayEquals(listA.subList(0, 2).toArray(a), listB.toArray(a));

        try {
            listB = blockingQueue.take(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertArrayEquals(listA.subList(2, 4).toArray(a), listB.toArray(a));
    }

    @Test
    public void test2() {
        Integer[] a = { 1, 2, 3, 4, 5, 6, 7 };
        List<Integer> listA = arrayToList(a);
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(7);

        try {
            blockingQueue.offer(arrayToList(a));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Integer> listB = new ArrayList<>();
        try {
            listB = blockingQueue.take(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertArrayEquals(listA.toArray(a), listB.toArray(a));
    }

    @Test
    public void blockingTest1() {
        Integer[] a = { 1, 2, 3 };
        Integer[] b = { 4, 5, 6 };
        Integer[] c = { 7, 8, 9 };
        Integer[] r = { 1, 2, 3, 4, 5, 6, 7 };

        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(15);

        try {
            Thread threadA = new Thread(new OfferThread(blockingQueue, arrayToList(a)));
            threadA.start();
            threadA.join();

            Thread threadB = new Thread(new OfferThread(blockingQueue, arrayToList(b)));
            threadB.start();
            threadB.join();

            Thread threadR = new Thread(new TakeThread(blockingQueue, 7, arrayToList(r)));
            threadR.start();

            Thread threadC = new Thread(new OfferThread(blockingQueue, arrayToList(c)));
            threadC.start();
            threadC.join();

            threadR.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void blockingTest2() {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(6);

        Integer[] a = { 1, 2, 3 };
        Integer[] b = { 4, 5, 6, 7 };
        Integer[] r1 = { 1, 2 };
        Integer[] r2 = { 3, 4, 5, 6, 7 };

        try {
            Thread threadA = new Thread(new OfferThread(blockingQueue, arrayToList(a)));
            threadA.start();
            threadA.join();

            Thread threadB = new Thread(new OfferThread(blockingQueue, arrayToList(b)));
            threadB.start();
//            threadB.join();

            Thread threadR1 = new Thread(new TakeThread(blockingQueue, 2, arrayToList(r1)));
            threadR1.start();
            threadR1.join();

            Thread threadR2 = new Thread(new TakeThread(blockingQueue, 5, arrayToList(r2)));
            threadR2.start();
            threadR2.join();

            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private class OfferThread implements Runnable {
        private final BlockingQueue<Integer> blockingQueue;
        private final List<Integer> offered;

        private OfferThread(BlockingQueue<Integer> blockingQueue, List<Integer> offered) {
            this.blockingQueue = blockingQueue;
            this.offered = offered;
        }

        @Override
        public void run() {
            try {
                blockingQueue.offer(offered);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }

    private class TakeThread implements Runnable {
        private final BlockingQueue<Integer> blockingQueue;
        private final int num;
        private final List<Integer> expected;

        private TakeThread(BlockingQueue<Integer> blockingQueue, int num, List<Integer> expected) {
            this.blockingQueue = blockingQueue;
            this.num = num;
            this.expected = expected;
        }

        @Override
        public void run() {
            try {
                List<Integer> result = blockingQueue.take(num);
                Assert.assertArrayEquals(expected.toArray(new Integer[0]), result.toArray(new Integer[0]));
            } catch (InterruptedException e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }
}
