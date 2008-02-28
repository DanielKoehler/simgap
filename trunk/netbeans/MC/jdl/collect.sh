#!/bin/sh
JID=$1
echo a | edg-job-get-output -i $JID
DATA=$(date +%Y%m%d%H%M%S)
FRNAME=$DATA-results.tar
if tar -cf $FRNAME /tmp/jobOutput/novelli_* &> /dev/null
then 
  bzip2 $FRNAME
fi

