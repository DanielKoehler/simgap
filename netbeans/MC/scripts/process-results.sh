#!/bin/sh
ALLRESULTS=$1
DEST=$2
WHICH=$3
rm -fR $HOME/tmp/jobOutput/
tar xvjf $ALLRESULTS
find $HOME/tmp/jobOutput/ -name results | xargs rm -fR
find $HOME/tmp/jobOutput/ -name results.tar.bz2 | xargs ls -l | grep 46 | awk '{print $8}' | awk -F "/results" '{print $1}' | xargs rm -fR
find $HOME/tmp/jobOutput/ -name results.tar.bz2 | xargs ls -l | awk '{print $8}' | awk -F "/results" '{print "tar xvjf " $1 "/results.tar.bz2 -C " $1}'  | bash
cd $HOME
find tmp/jobOutput/ -name ReF_CR.csv | awk -F "/results/" '{print "sh scripts/ReF_CR.sh " $1"/results/"$2"xxx"}' | sed s/\.csvxxx/xxx/g  | sed s/tmp/\"tmp/g | sed s/ReF_CRxxx/ReF_CR\"/g | bash ; 
find tmp/jobOutput/ -name ReF_RT.csv | awk -F "/results/" '{print "sh scripts/ReF_RT.sh " $1"/results/"$2"xxx"}' | sed s/\.csvxxx/xxx/g  | sed s/tmp/\"tmp/g | sed s/ReF_RTxxx/ReF_RT\"/g | bash
find tmp/jobOutput/ -name USERS_*.csv | awk -F "/results/" '{print "sh scripts/USERS.sh " $1"/results/"$2"xxx"}' | sed s/\.csvxxx/xxx/g  | sed s/tmp/\"tmp/g | sed s/xxx/\"/g | bash
rm -fR results
mkdir results
find tmp/jobOutput/ -name *.conf | awk -F ".conf" '{print $1}' | awk -F "/results/" '{print "DIR="$1 "; NAME=" $2"; SUFFIX=$(echo $NAME | sed s/QAGESA//g | sed s/_//g | sed s/[0-9]*//g); mkdir -p results/$SUFFIX; cp -fR $DIR/results/* results/$SUFFIX; sh scripts/trunk.sh results/$SUFFIX "}' | bash
rm -fR $DEST &> /dev/null
mv results $DEST
#NN=$(N=$(cat $DEST/ReF_CR.dat  | wc | awk '{print $1 " - 1"}') ; echo $N | bc); cat $DEST/ReF_CR.dat | tail -n $NN > /tmp/ReF_CR.dat
#rm $DEST/ReF_CR.dat &> /dev/null
#mv /tmp/ReF_CR.dat $DEST/ReF_CR.dat
rm $DEST.tar &> /dev/null
tar -cf $DEST.tar $DEST
rm $DEST.tar.bz2 &> /dev/null
bzip2 $DEST.tar
