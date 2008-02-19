#!/bin/sh
ALLRESULTS=$1
rm -fR $HOME/tmp/jobOutput/
tar xvjf $ALLRESULTS
find $HOME/tmp/jobOutput/ -name results | xargs rm -fR
find $HOME/tmp/jobOutput/ -name results.tar.bz2 | xargs ls -l | grep 46 | awk '{print $8}' | awk -F "/results" '{print $1}' | xargs rm -fR
find $HOME/tmp/jobOutput/ -name results.tar.bz2 | xargs ls -l | awk '{print $8}' | awk -F "/results" '{print "tar xvjf " $1 "/results.tar.bz2 -C " $1}'  | bash
find tmp/jobOutput/ -name ReF_CR.csv | awk -F "/results/" '{print "sh scripts/ReF_CR.sh " $1"/results/"$2"xxx"}' | sed s/\.csvxxx/xxx/g  | sed s/tmp/\"tmp/g | sed s/ReF_CRxxx/ReF_CR\"/g | bash
find tmp/jobOutput/ -name ReF_RT.csv | awk -F "/results/" '{print "sh scripts/ReF_RT.sh " $1"/results/"$2"xxx"}' | sed s/\.csvxxx/xxx/g  | sed s/tmp/\"tmp/g | sed s/ReF_RTxxx/ReF_RT\"/g | bash
