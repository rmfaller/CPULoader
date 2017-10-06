/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpuloader;

import java.util.Date;

/**
 *
 * @author rmfaller
 */
public class CPULoader extends Thread {

    private static long lapsedtime = 500;
    private static long threads = 1;
    private static int threshold = 0;
    private static int defaultthreshold = 5;
    private static boolean csv = false;
    private static boolean forever = false;

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
                case "-c":
                case "--csv":
                    csv = true;
                    break;
                case "-f":
                case "--forever":
                    forever = true;
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
            int cores = Runtime.getRuntime().availableProcessors();
            Spinner spinner = new Spinner(128);
            if (threshold == 0) {
                System.out.println("CPULoader found " + cores + " cores and running " + threads + " threads for " + lapsedtime + "ms with threshold = " + threshold);
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
                int loops = 0;
                while (loops <= 1) {
                    long threadcnt = threads;
                    long tasktime = 0;
//                System.out.println("CPULoader found " + cores + " cores and starting with " + threadcnt + " thread(s) and not to exceed " + lapsedtime + "ms total loop time per thread");
                    if (!csv) {
                        System.out.println("CPULoader found " + cores + " cores and starting with " + threadcnt + " thread(s) and not to exceed " + lapsedtime + "ms total loop time per thread");
                    } else {
                        if (loops == 0) {
                            System.out.println("timestamp,threads,avr-time,passed,exceeded,threshold,");
                        }
                    }
                    while (threshold >= (tasktime / (float) threadcnt)) {
                        Loaders[] loaders = new Loaders[(int) threadcnt];
                        for (int i = 0; i < threadcnt; i++) {
                            loaders[i] = new Loaders(i, lapsedtime, threshold);
                            loaders[i].start();
                        }
                        tasktime = 0;
                        int exceed = 0;
                        int passed = 0;
                        for (int i = 0; i < threadcnt; i++) {
                            loaders[i].join();
                            tasktime = loaders[i].getTaskTime() + tasktime;
                            if (loaders[i].getTaskTime() > threshold) {
                                exceed++;
                            } else {
                                passed++;
                            }
                        }
                        if (csv) {
                            System.out.println((long) new Date().getTime() + "," + threadcnt + "," + (float) (tasktime / (float) threadcnt) + "," + passed + "," + exceed + "," + threshold);
                        } else {
                            System.out.print("Thread count = ");
                            System.out.format("%4d", threadcnt);
                            System.out.print(" with average time to run = ");
                            System.out.format("%5.2f", (float) (tasktime / (float) threadcnt));
                            System.out.println("ms and " + exceed + " exceeded the " + threshold + "ms threshold");
                        }
                        threadcnt++;
                    }
                    if (!forever) {
                        loops = 2;
                    } else {
                        loops = 1;
                        threadcnt = threads;
                        tasktime = 0;
                    }
                }
            }
        }
    }

    private static void help() {
        String help = "\nCPULoader usage:"
                + "\njava -jar ./dist/CPULoader.jar"
                + "\navailable options:"
                + "\n\t--lapsedtime | -l {default = " + lapsedtime + "} time, in milliseconds the load should run - OR - how many iterations a thread should make when used with --forever"
                + "\n\t--threads    | -t {default = " + threads + "} maximum number of threads to spawn"
                + "\n\t--threshold  | -s {default = " + defaultthreshold + "} milliseconds a thread must complete by; once exceeded stop the thread"
                + "\n\t--csv        | -c {default = non-csv output} outputs in comma-delimited format"
                + "\n\t--forever    | -f {default is to NOT run forever}"
                + "\n\t--help       | -h this output\n"
                + "\nExample: java -jar ./dist/CPULoader.jar --lapsedtime 20000 --threads 2\n";
        System.out.println(help);
    }
}
