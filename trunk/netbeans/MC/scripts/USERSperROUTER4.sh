# Select results for USER attached to router I with I=0..NROUTERS-1 where NROUTERS is number of routers
#!/bin/sh
WHERE=$1
NAME=$2
FNAME=$1/$2.csv
NROUTERS=$3
ROUTER=$4
FINAL=$WHERE/USERS_OF_4_$ROUTER.dat
cat $FNAME | grep ";1;" | awk -F ";" '{print $7 " " $8-1000.0 " " $9-1000.0 " " $10}' | awk -F "_" '{print $2}' | awk '{print $1%4 " " $2 " " $3 " " $4}' |  sort -n -k 2 | grep "^$ROUTER" > $FINAL
