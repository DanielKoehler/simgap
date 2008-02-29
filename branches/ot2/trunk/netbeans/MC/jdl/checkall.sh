#!/bin/sh
ls -l $1* | awk '{print "jdl/check.sh " $9 " | grep Current"}' | bash
