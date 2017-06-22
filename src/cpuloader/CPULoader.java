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

    private static long lapsedtime = 5000;
    private static long threads = 1;

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
            System.out.println("CPULoader running " + threads + " threads for " + lapsedtime + "ms. ");
            Spinner spinner = new Spinner(128);
            Loaders[] loaders = new Loaders[(int) threads];
            spinner.start();
            for (int i = 0; i < threads; i++) {
                loaders[i] = new Loaders(i, lapsedtime);
                loaders[i].start();
            }
            for (int i = 0; i < threads; i++) {
                loaders[i].join();
            }
            spinner.continueToRun = false;
            System.out.println(". Stopped and completed.");
        }
    }

    private static void help() {
        String help = "\nCPULoader usage:"
                + "\njava -jar ./dist/CPULoader.jar"
                + "\navailable options:"
                + "\n\t--lapsedtime | -l {default = 5000} time, in milliseconds the load should run"
                + "\n\t--threads    | -t {default = 1} numbers of threads to spawn"
                + "\n\t--help       | -h this output\n"
                + "\nExample: java -jar ./dist/CPULoader.jar --lapsedtime 20000 --threads 2\n";
        System.out.println(help);
    }
}
