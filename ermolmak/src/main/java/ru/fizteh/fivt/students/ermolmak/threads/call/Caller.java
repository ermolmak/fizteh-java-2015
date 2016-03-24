package ru.fizteh.fivt.students.ermolmak.threads.call;

class Caller implements Runnable {
    private final int numberOfThreads;
    private final boolean[] answers;
    private final Object monitor;
    private Turn turn;
    private int unansweredThreads;
    private int yesCounter;

    Caller(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        answers = new boolean[numberOfThreads];
        monitor = new Object();
    }

    private void reset() {
        unansweredThreads = numberOfThreads;
        yesCounter = 0;
        for (int i = 0; i < numberOfThreads; ++i) {
            answers[i] = false;
        }
    }

    @Override
    public void run() {
        turn = Turn.MAIN_THREAD;
        for (int i = 0; i < numberOfThreads; ++i) {
            (new Thread(new ChildThread(i))).start();
        }
        reset();

        while (yesCounter != numberOfThreads) {
            synchronized (monitor) {
                System.out.println("Are you ready?");
                turn = Turn.CHILD_THREAD;
                monitor.notifyAll();
                while (turn == Turn.CHILD_THREAD) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (yesCounter != numberOfThreads) {
                    reset();
                }
            }
        }
    }

    private enum Turn {
        MAIN_THREAD,
        CHILD_THREAD
    }

    private class ChildThread implements Runnable {
        private final int myNumber;

        ChildThread(int myNumber) {
            this.myNumber = myNumber;
        }

        private boolean getAnswer() {
            return Math.random() < 0.9;
        }

        @Override
        public void run() {
            while (yesCounter != numberOfThreads) {
                synchronized (monitor) {
                    while (turn == Turn.MAIN_THREAD) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    boolean answer = getAnswer();
                    if (answer) {
                        ++yesCounter;
                        System.out.println("Yes");
                    } else {
                        System.out.println("No");
                    }
                    --unansweredThreads;
                    answers[myNumber] = true;

                    if (unansweredThreads == 0) {
                        turn = Turn.MAIN_THREAD;
                        monitor.notifyAll();
                        continue;
                    }

                    while (answers[myNumber] & yesCounter != numberOfThreads) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
