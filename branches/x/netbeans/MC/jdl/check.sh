#!/bin/sh
JID=$1
JIDOUT=$1_OK
echo a | edg-job-status -i $JID | grep Status  | grep -v Reason   | grep Status | awk -F ":" '{l=NR % 12; var+=($2=="     Done (Success)");  if ((NR%2)==1) {print "https:" $3 ":" $4}; if (l==11) {es=1; print var; var=0}}'  | grep -B 6 "^6" | grep -v "^6" | grep -v "^--" > $JIDOUT
echo "The following jobs have been completed:"
cat $JIDOUT
