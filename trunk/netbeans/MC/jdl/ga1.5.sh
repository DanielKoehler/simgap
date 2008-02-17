#!/bin/sh
cp ../MC/conf/*.conf .
ls -l ../MC/conf | grep -v total | awk '{print "sed s/QAGESA.conf/"$9"/g run1.5g.jdl > "$9".jdl; edg-job-submit --vo cometa -o $HOME/jid "$9".jdl"'
