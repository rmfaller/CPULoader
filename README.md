# CPULoader

```
CPULoader usage:
java -jar ./dist/CPULoader.jar
available options:
	--lapsedtime | -l {default = 500} time, in milliseconds the load should run - OR - how long an individual thread should run when threshold is set to 0
	--minthreads | -m {default = 1} minimum number of threads to spawn
	--maxthreads | -x {default = 1} maximum number of threads to spawn
	--threshold  | -s {default = 0} milliseconds a thread must complete by; once exceeded stop the thread
	--csv        | -c {default = non-csv output} outputs in comma-delimited format
	--forever    | -f {default = false i.e. do  NOT run forever} include this switch to set forever to true
	--baseline   | -b {default = false} repeat test while incrementing threads until response time exceeds threshold
	--help       | -h this output

Examples:

Run load with a set of threads (number of reported cores) until lapsedtime is met:
java -jar ./dist/CPULoader.jar --lapsedtime 20000

Run 12 threads of load until lapsedtime is met:
java -jar ./dist/CPULoader.jar --lapsedtime 20000 --maxthreads 12

Increment thread count up to number of cores reporting how many threads exceeded the threshold:
java -jar ./dist/CPULoader.jar --threshold 38

Increment thread count up to maxthreads reporting how many threads exceeded the threshold:
java -jar ./dist/CPULoader.jar --threshold 38 --maxthreads 12

Find threshold sweet spot - set maxthreads slightly larger than known cores and increment threshold until 0% - 10% threads exceed threshold:
java -jar ./dist/CPULoader.jar --threshold 38 --maxthreads 12 --forever

Find CPU consistency - set maxthreads to 2x - 3x known cores and threshold to "sweet spot" as discovered using the previous example:
java -jar ./dist/CPULoader.jar --threshold 38 --maxthreads 12 --forever --baseline

Increment thread count until threshold or maxthreads is exceeded and start again until stopped with output in csv format (handy for graphing):
java -jar ./dist/CPULoader.jar --threshold 38 --maxthreads 22 --forever --csv

```
