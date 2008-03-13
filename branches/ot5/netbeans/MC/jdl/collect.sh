#!/bin/sh
JID=$1
echo a | edg-job-get-output -i $JID
find /tmp/jobOutput -name *.conf | awk -F "/QAGESA_" '{print "WHERE=" $1 "; NAME=$(echo "$2" | sed s/\.conf//g); ls $WHERE; echo $NAME; mv $WHERE/results.tar.bz2 $WHERE/$NAME.tar.bz2"}' | bash 
DATA=$(date +%Y%m%d%H%M%S)
FRNAME=$DATA-results.tar
if tar -cf $FRNAME /tmp/jobOutput/novelli_* &> /dev/null
then 
  bzip2 $FRNAME
fi

