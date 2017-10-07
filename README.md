# CPULoader

CPULoader usage: **java -jar ./dist/CPULoader.jar**

available options:

	--lapsedtime | -l {default = 500} time, in milliseconds the load should run - OR - how long an individual thread should run when threshold is set to 0

	--threads    | -t {default = 1} maximum number of threads to spawn

	--threshold  | -s {default = 0} milliseconds a thread must complete by; once exceeded stop the thread

	--csv        | -c {default = non-csv output} outputs in comma-delimited format

	--forever    | -f {default = false i.e. do  NOT run forever} include this switch to set forever to true

	--help       | -h this output

Examples:

Run 2 threads of load until stopped:

**java -jar ./dist/CPULoader.jar --lapsedtime 20000 --threads 2**

Increment thread count until threshold is exceeded:

**java -jar ./dist/CPULoader.jar --threshold 5**

Increment thread count until threshold is exceeded and start again until stopped with output in csv format:

**java -jar ./dist/CPULoader.jar --threshold 5 --forever --csv**
