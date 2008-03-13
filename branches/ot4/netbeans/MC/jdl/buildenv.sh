#!/bin/sh
LOCAL_JRE=$1
CONF=$2
#MEM=$(cat /proc/meminfo  | grep MemTotal | awk '{print $2"*.68"}' | bc | awk -F "." '{print $1}')
MMIN=$3
MMAX=$4
RESULTSNAME=results
REMOTEFILE_JRE=$(cat $LOCAL_JRE)
LOCALFILE_JRE=$(basename $REMOTEFILE_JRE)
LFC_HOST=infn-se-01.ct.pi2s2.it
echo "INFO:  Getting bzipped JRE to $PWD/$LOCALFILE_JRE from lfn:$REMOTEFILE_JRE at LFC_HOST=$LFC_HOST"
lcg-cp --vo cometa lfn:$REMOTEFILE_JRE file://$PWD/$LOCALFILE_JRE 
echo "INFO:  Extracting bzipped JRE $PWD/$LOCALFILE_JRE to $PWD/java"
rm -fR $PWD/java 
tar xvjf $LOCALFILE_JRE 
export PATH=$PWD/java/bin:$PATH
unzip MC.zip -d MC
cd MC
sh run.sh $CONF $MMIN $MMAX
tar -cf $RESULTSNAME.tar $RESULTSNAME
bzip2 -9 $RESULTSNAME.tar
cp $RESULTSNAME.tar.bz2 ..
