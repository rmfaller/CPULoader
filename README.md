# CPULoader

CPULoader usage: **java -jar ./dist/CPULoader.jar**

available options:

`--lapsedtime | -l {default = 500} time, in milliseconds the load should run`

`--threads    | -t {default = 1} numbers of threads to spawn`

`--threshold  | -s {default = 5} milliseconds a thread must complete by; once exceeded stop the thread`

`--csv        | -c {default = non-csv output} outputs in comma-delimited format when using --threshold`

`--help       | -h this output`


Example: 
**java -jar ./dist/CPULoader.jar --lapsedtime 20000 --threads 2**
