#!/bin/sh
ALLRESULTS=$1
rm -fR $HOME/tmp/jobOutput/
tar xvjf $ALLRESULTS
find $HOME/tmp/jobOutput/ -name results | xargs rm -fR
find $HOME/tmp/jobOutput/ -name results.tar.bz2 | xargs ls -l | grep 46 | awk '{print $8}' | awk -F "/results" '{print $1}' | xargs rm -fR
find $HOME/tmp/jobOutput/ -name results.tar.bz2 | xargs ls -l | awk '{print $8}' | awk -F "/results" '{print "tar xvjf " $1 "/results.tar.bz2 -C " $1}'  | bash
