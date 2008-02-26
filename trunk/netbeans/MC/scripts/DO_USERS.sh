#!/bin/sh
SCRIPTS=$1
WHERE=$2
NROUTERS=$3
ROUTER=0
while [ $ROUTER -ne $NROUTERS ]
do
  sh $SCRIPTS/USERSperROUTER.sh $WHERE USERS_MS  $NROUTERS $ROUTER
  ROUTER=$(( $ROUTER + 1 ))
done

rm $WHERE/USERS_MEAN.dat &> /dev/null

ROUTER=0
while [ $ROUTER -ne $NROUTERS ]
do
  cat $WHERE/USERS_$ROUTER.dat | awk -f $SCRIPTS/USERS_AVG.awk >> $WHERE/USERS_MEAN.dat
  ROUTER=$(( $ROUTER + 1 ))
done
