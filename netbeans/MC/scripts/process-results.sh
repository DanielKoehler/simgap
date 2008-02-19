#!/bin/sh
ALLRESULTS=$1
DEST=$2
rm -fR $HOME/tmp/jobOutput/
tar xvjf $ALLRESULTS
find $HOME/tmp/jobOutput/ -name results | xargs rm -fR
find $HOME/tmp/jobOutput/ -name results.tar.bz2 | xargs ls -l | grep 46 | awk '{print $8}' | awk -F "/results" '{print $1}' | xargs rm -fR
find $HOME/tmp/jobOutput/ -name results.tar.bz2 | xargs ls -l | awk '{print $8}' | awk -F "/results" '{print "tar xvjf " $1 "/results.tar.bz2 -C " $1}'  | bash
cd $HOME
find tmp/jobOutput/ -name ReF_CR.csv | awk -F "/results/" '{print "sh scripts/ReF_CR.sh " $1"/results/"$2"xxx"}' | sed s/\.csvxxx/xxx/g  | sed s/tmp/\"tmp/g | sed s/ReF_CRxxx/ReF_CR\"/g | bash
find tmp/jobOutput/ -name ReF_RT.csv | awk -F "/results/" '{print "sh scripts/ReF_RT.sh " $1"/results/"$2"xxx"}' | sed s/\.csvxxx/xxx/g  | sed s/tmp/\"tmp/g | sed s/ReF_RTxxx/ReF_RT\"/g | bash
find tmp/jobOutput/ -name USERS_*.csv | awk -F "/results/" '{print "sh scripts/USERS.sh " $1"/results/"$2"xxx"}' | sed s/\.csvxxx/xxx/g  | sed s/tmp/\"tmp/g | sed s/xxx/\"/g | bash
rm -fR results
mkdir results
find tmp/jobOutput -name *.csv | awk '{print "cp " $1 " results/"}' | bash
find tmp/jobOutput -name *.dat | awk '{print "cp " $1 " results/"}' | bash
find tmp/jobOutput -name *.conf | awk '{print "cp " $1 " results/"}' | bash
mv results $DEST
find $DEST -name *.dat | xargs ls -l | awk '{print $8}' | awk -F "." '{print "cat " $1 ".dat | grep \"^1 \" > " $1 "_1.dat"}' | bash
NN=$(N=$(cat $DEST/ReF_CR_1.dat  | wc | awk '{print $1 " - 1"}') ; echo $N | bc); cat $DEST/ReF_CR_1.dat | tail -n $NN > /tmp/ReF_CR_1.dat
rm $DEST/ReF_CR_1.dat
mv /tmp/ReF_CR_1.dat $DEST/ReF_CR_1.dat
bzip2 $DEST.tar
