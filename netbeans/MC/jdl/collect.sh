#!/bin/sh
DATA=$(date +%Y%m%d%h%M%S)
FRNAME=$DATA-results.tar
tar -cf $FRNAME /tmp/jobOutput/novelli_*
bzip2 $FRNAME

