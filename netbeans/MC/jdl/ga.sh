#!/bin/sh
JID=$1
sh ga1.sh $JID | sed s/\$JID/\$HOME\\/$1/g
sh ga2.sh $JID | sed s/\$JID/\$HOME\\/$1/g
sh ga3.sh $JID | sed s/\$JID/\$HOME\\/$1/g

