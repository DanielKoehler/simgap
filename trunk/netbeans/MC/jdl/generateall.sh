#!/bin/sh
cp ../MC/conf/*.conf .
ls -l ../MC/conf | grep -v total | awk '{print "sed s/QAGESA/"$9"/g run.jdl > "$9".jdl; edg-job-submit --vo cometa -o $HOME/jid "$9".jdl"}'
