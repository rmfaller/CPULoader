/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpuloader;

import static java.lang.Math.atan;
import static java.lang.Math.tan;
import static java.lang.Thread.yield;
import java.util.Date;

/**
 *
 * @author rmfaller
 */
class Loaders extends Thread {

    private final long lapsedtime;
    private final int threadid;

    Loaders(int t, long lapsedtime) {
        this.threadid = t;
        this.lapsedtime = lapsedtime;
    }

    @Override
    public void run() {
        long optime = 0;
        long startop = (long) new Date().getTime();
        while ((optime <= lapsedtime) || (lapsedtime == 0)) {
            Math.atan(Math.sqrt(Math.pow(32768, 32768)));
            double d = tan(atan(tan(atan(tan(atan(tan(atan(tan(atan(123456789.123456789))))))))));
            fibber(16);
            long endop = (long) new Date().getTime();
            optime = (endop - startop);
        }
        yield();
    }

    public static long fibber(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibber(n - 1) + fibber(n - 2);
        }
    }
}
