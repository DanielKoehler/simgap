#!/bin/sh
JID=$1
cp ../MC/conf/*.conf .
ls -l ../MC/conf | grep -v total | awk '{print "sed s/QAGESA.conf/"$9"/g run3g.jdl > "$9".jdl; edg-job-submit --vo cometa -o $JID "$9".jdl"}'
