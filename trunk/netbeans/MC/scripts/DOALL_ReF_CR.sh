#!/bin/sh
WHERE=$1
find $WHERE -name ReF_CR.dat | awk -F "." '{print "sh scripts/ReF_CR.sh "$1}' 
#cat MF/sim_out.txt | grep -B 1000000 "Performing replication #2" | grep "END_REPLY" | grep SUCC  | awk ' {print "1 " $1-1000.0 " " NR}' > MF/completed.dat

