#!/bin/bash
#
# script
#
echo "Starting at: "$(date +%Y%m%d%H%M%S)

HOSTNAME=$(hostname -f)
USER=$(whoami)
ARG1=$1
LOCALDIR=$(pwd)

echo "****************************************"
echo "HOST: "$HOSTNAME
echo "USER: "$USER
echo "ARGS: "$ARG1
echo "LOCALDIR is: "$LOCALDIR
echo "HOMEDIR is:"$HOME
echo "Content of home:"
ls -l $HOME
echo "Content of current dir:"
ls -l .
echo "****************************************"

# view proxy info
voms-proxy-info --all

#
# Wait for more than one hour 1h and 30 minutes
#
for i in $(seq 1 90)
do
  printf "Waiting 60 sec ... "
  sleep 60
  echo "done"
done

# view proxy info
voms-proxy-info --all

echo "Ending at: "$(date +%Y%m%d%H%M%S)
