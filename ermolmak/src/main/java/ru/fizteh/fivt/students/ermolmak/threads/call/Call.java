package ru.fizteh.fivt.students.ermolmak.threads.call;

public class Call {
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                int num = Integer.parseInt(args[0]);
                if (num > 0) {
                    (new Thread(new Caller(num))).start();
                } else {
                    printUsage();
                }
            } catch (NumberFormatException e) {
                printUsage();
            }
        } else {
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usase: Call {number of threads}");
    }
}
