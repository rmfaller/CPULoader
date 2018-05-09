# CPULoader

```

CPULoader usage:
java -jar ./dist/CPULoader.jar --help

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

Run 2 threads of load for 2000 milliseconds:
java -jar ./dist/CPULoader.jar --lapsedtime 2000 --maxthreads 2

Increment thread count until threshold is exceeded:
java -jar ./dist/CPULoader.jar --threshold 36

Find threshold sweet spot - set maxthreads to known cores and increment threshold until 0% - 10% threads exceed threshold:
java -jar ./dist/CPULoader.jar  --maxthreads 8 --forever --threshold 38

Find CPU consistency - set maxthreads to 2x - 3x known cores and threshold to "sweet spot":
java -jar ./dist/CPULoader.jar  --maxthreads 22 --forever --threshold 38 --baseline

Increment thread count until threshold is exceeded and start again until stopped with output in csv format:
java -jar ./dist/CPULoader.jar --threshold 36 --forever --csv --maxhtreads 32

```
