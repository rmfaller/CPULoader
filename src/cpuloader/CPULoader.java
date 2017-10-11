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
    private static long minthreads = 1;
    private static long maxthreads = 1;
    private static int threshold = 0;
    private static final int defaultthreshold = 5;
    private static boolean csv = false;
    private static boolean forever = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
/*                case "-t":
                case "--threads":
                    threads = Long.parseLong(args[i + 1]);
                    break; */
                case "-m":
                case "--minthreads":
                    minthreads = Long.parseLong(args[i + 1]);
                    break;
                case "-x":
                case "--maxthreads":
                    maxthreads = Long.parseLong(args[i + 1]);
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
        if (minthreads > maxthreads) {
            maxthreads = minthreads + 1;
        }
        if (threads > 0) {
            int cores = Runtime.getRuntime().availableProcessors();
            Spinner spinner = new Spinner(128);
            if (threshold == 0) {
                if (forever) {
                    lapsedtime = 0;
                }
                threads = maxthreads;
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
                    long threadcnt = minthreads;
                    long tasktime = 0;
                    if (!csv) {
                        System.out.println("CPULoader found " + cores + " cores and starting with " + threadcnt + " thread(s) and not to exceed " + lapsedtime + "ms total loop time per thread");
                    } else {
                        if (loops == 0) {
                            System.out.println("timestamp,threads,avr-time,passed,exceeded,threshold,");
                        }
                    }
                    while ((threshold >= (tasktime / (float) threadcnt)) && (threadcnt <= maxthreads)) {
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
                        threadcnt = minthreads;
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
                + "\n\t--lapsedtime | -l {default = " + lapsedtime + "} time, in milliseconds the load should run - OR - how long an individual thread should run when threshold is set to 0"
//                + "\n\t--threads    | -t {default = " + threads + "} maximum number of threads to spawn"
                + "\n\t--minthreads | -m {default = " + threads + "} maximum number of threads to spawn"
                + "\n\t--maxthreads | -x {default = " + threads + "} maximum number of threads to spawn"
                + "\n\t--threshold  | -s {default = " + threshold + "} milliseconds a thread must complete by; once exceeded stop the thread"
                + "\n\t--csv        | -c {default = non-csv output} outputs in comma-delimited format"
                + "\n\t--forever    | -f {default = " + forever + " i.e. do  NOT run forever} include this switch to set forever to true"
                + "\n\t--help       | -h this output\n"
                + "\nExamples:"
                + "\n\nRun 2 threads of load until stopped:"
                + "\njava -jar ./dist/CPULoader.jar --lapsedtime 20000 --threads 2"
                + "\n\nIncrement thread count until threshold is exceeded:"
                + "\njava -jar ./dist/CPULoader.jar --threshold 5"
                + "\n\nIncrement thread count until threshold is exceeded and start again until stopped with output in csv format:"
                + "\njava -jar ./dist/CPULoader.jar --threshold 5 --forever --csv\n";
        System.out.println(help);
    }
}
