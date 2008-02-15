#!/bin/sh
echo a | edg-job-get-output -i $HOME/jid
DATA=$(date +%Y%m%d%h%M%S)
FRNAME=$DATA-results.tar
tar -cf $FRNAME /tmp/jobOutput/novelli_*
bzip2 $FRNAME

