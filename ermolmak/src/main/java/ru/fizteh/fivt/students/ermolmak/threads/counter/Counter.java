package ru.fizteh.fivt.students.ermolmak.threads.counter;


public class Counter {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
        } else {
            try {
                int numberOfThreads = Integer.parseInt(args[0]);
                if (numberOfThreads > 0) {
                    new CounterLauncher(numberOfThreads);
                } else {
                    printUsage();
                }
            } catch (NumberFormatException e) {
                printUsage();
            }
        }
    }

    static void printUsage() {
        System.out.println("Usage: Counter {number of threads}");
    }
}
