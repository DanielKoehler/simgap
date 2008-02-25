#!/bin/sh
SCRIPTS=$1
sh $SCRIPTS/ReF_CR.sh MF/ReF_CR
sh $SCRIPTS/ReF_CR.sh MR/ReF_CR
sh $SCRIPTS/ReF_CR.sh MS/ReF_CR
sh $SCRIPTS/ReF_CR.sh RMF/ReF_CR
sh $SCRIPTS/ReF_CR.sh RMR/ReF_CR
sh $SCRIPTS/ReF_CR.sh RMS/ReF_CR
sh $SCRIPTS/ReF_CR_ALL.sh MF/ReF_CR
sh $SCRIPTS/ReF_CR_ALL.sh MR/ReF_CR
sh $SCRIPTS/ReF_CR_ALL.sh MS/ReF_CR
sh $SCRIPTS/ReF_CR_ALL.sh RMF/ReF_CR
sh $SCRIPTS/ReF_CR_ALL.sh RMR/ReF_CR
sh $SCRIPTS/ReF_CR_ALL.sh RMS/ReF_CR
cat MF/sim_out.txt | grep -B 1000000 "Performing replication #2" | grep "END_REPLY" | grep SUCC  | awk ' {print "1 " $1-1000.0 " " NR}' > MF/completed.dat
cat MR/sim_out.txt | grep -B 1000000 "Performing replication #2" | grep "END_REPLY" | grep SUCC  | awk ' {print "1 " $1-1000.0 " " NR}' > MR/completed.dat
cat MS/sim_out.txt | grep -B 1000000 "Performing replication #2" | grep "END_REPLY" | grep SUCC  | awk ' {print "1 " $1-1000.0 " " NR}' > MS/completed.dat
cat RMF/sim_out.txt | grep -B 1000000 "Performing replication #2" | grep "END_REPLY" | grep SUCC  | awk ' {print "1 " $1-1000.0 " " NR}' > RMF/completed.dat
cat RMR/sim_out.txt | grep -B 1000000 "Performing replication #2" | grep "END_REPLY" | grep SUCC  | awk ' {print "1 " $1-1000.0 " " NR}' > RMR/completed.dat
cat RMS/sim_out.txt | grep -B 1000000 "Performing replication #2" | grep "END_REPLY" | grep SUCC  | awk ' {print "1 " $1-1000.0 " " NR}' > RMS/completed.dat

