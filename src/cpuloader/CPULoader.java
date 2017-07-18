/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpuloader;

/**
 *
 * @author rmfaller
 */
public class CPULoader extends Thread {

    private static long lapsedtime = 500;
    private static long threads = 1;
    private static int threshold = 0;
    private static int defaultthreshold = 5;

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-t":
                case "--threads":
                    threads = Long.parseLong(args[i + 1]);
                    break;
                case "-l":
                case "--lapsedtime":
                    lapsedtime = Long.parseLong(args[i + 1]);
                    break;
                case "-s":
                case "--threshold":
                    if ((i + 1) < args.length) {
                        threshold = Integer.parseInt(args[i + 1]);
                    } else {
                        threshold = defaultthreshold;
                    }
                    break;
                case "-h":
                case "--help":
                    help();
                    threads = 0;
                    break;
                default:
                    break;
            }
        }
        if (threads > 0) {
            Spinner spinner = new Spinner(128);
            if (threshold == 0) {
                System.out.println("CPULoader running " + threads + " threads for " + lapsedtime + "ms with threshold = " + threshold);
                Loaders[] loaders = new Loaders[(int) threads];
                spinner.start();
                for (int i = 0; i < threads; i++) {
                    loaders[i] = new Loaders(i, lapsedtime, threshold);
                    loaders[i].start();
                }
                for (int i = 0; i < threads; i++) {
                    loaders[i].join();
                }
                spinner.continueToRun = false;
                System.out.println(". Stopped and completed.");
            } else {
                long threadcnt = threads;
                long tasktime = 0;
                System.out.println("CPULoader starting with " + threadcnt + " thread(s) and not to exceed " + lapsedtime + "ms total loop time per thread");
                while (threshold >= (tasktime / (float) threadcnt)) {
                    Loaders[] loaders = new Loaders[(int) threadcnt];
                    for (int i = 0; i < threadcnt; i++) {
                        loaders[i] = new Loaders(i, lapsedtime, threshold);
                        loaders[i].start();
                    }
                    tasktime = 0;
                    int exceed = 0;
                    for (int i = 0; i < threadcnt; i++) {
                        loaders[i].join();
                        tasktime = loaders[i].getTaskTime() + tasktime;
                        if (loaders[i].getTaskTime() > threshold) {
                            exceed++;
                        }
                    }
                    System.out.print("Thread count = ");
                    System.out.format("%4d", threadcnt);
                    System.out.print(" with average time to run = ");
                    System.out.format("%5.2f", (float) (tasktime / (float) threadcnt));
                    System.out.println("ms and " + exceed + " exceeded the " + threshold + "ms threshold");
                    threadcnt++;
                }
            }
        }
    }

    private static void help() {
        String help = "\nCPULoader usage:"
                + "\njava -jar ./dist/CPULoader.jar"
                + "\navailable options:"
                + "\n\t--lapsedtime | -l {default = " + lapsedtime + "} time, in milliseconds the load should run"
                + "\n\t--threads    | -t {default = " + threads + "} numbers of threads to spawn"
                + "\n\t--threshold  | -s {default = " + defaultthreshold + "} milliseconds a thread must complete by; once exceeded stop the thread"
                + "\n\t--help       | -h this output\n"
                + "\nExample: java -jar ./dist/CPULoader.jar --lapsedtime 20000 --threads 2\n";
        System.out.println(help);
    }
}
