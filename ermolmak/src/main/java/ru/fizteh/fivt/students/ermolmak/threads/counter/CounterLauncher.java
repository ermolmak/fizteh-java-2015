package ru.fizteh.fivt.students.ermolmak.threads.counter;

class CounterLauncher {
    private int numberOfThreads;
    private Integer currentThread;
    private Object monitor;

    CounterLauncher(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.currentThread = 0;
        monitor = new Object();

        for (int i = 0; i < numberOfThreads; ++i) {
            (new Thread(new CounterThread(i))).start();
        }
    }

    class CounterThread implements Runnable {
        private int myNum;

        CounterThread(int number) {
            myNum = number;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (monitor) {
                    while (currentThread != myNum) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ++currentThread;
                    System.out.println("Thread-" + currentThread);
                    currentThread %= numberOfThreads;
                    monitor.notifyAll();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
