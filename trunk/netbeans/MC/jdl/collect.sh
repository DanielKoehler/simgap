#!/bin/sh
echo a | edg-job-get-output -i $HOME/jid
DATA=$(date +%Y%m%d%H%M%S)
FRNAME=$DATA-results.tar
if tar -cf $FRNAME /tmp/jobOutput/novelli_* &> /dev/null
then 
  bzip2 $FRNAME
fi

