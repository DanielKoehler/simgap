# Select results for USER attached to router I with I=0..NROUTERS-1 where NROUTERS is number of routers
#!/bin/sh
WHICH=$1
NROUTERS=$2
ROUTER=$3
cat $WHICH/USERS_$WHICH.csv | grep ";1;" | awk -F ";" '{print $7 " " $8-1000.0 " " $9-1000.0 " " $10}' | awk -F "_" '{print $2}' | awk '{print $1%4 " " $2 " " $3 " " $4}' |  sort -n -k 2 | grep "^$ROUTER"
