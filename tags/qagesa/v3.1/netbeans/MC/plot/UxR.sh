# Select results for USER attached to router I with I=0..NROUTERS-1 where NROUTERS is number of routers
#!/bin/sh
SCRIPTS=$1
WHERE=$2
SUFFIX=$3
FILE=USERS_$SUFFIX
FNAMECSV=$WHERE/$FILE.csv
FNAME=$2/USERS.dat
NROUTERS=$4

cat $FNAMECSV | sort -n -k 8 > $FNAME

ROUTER=0
while [ $ROUTER -ne $NROUTERS ]
do
	FINAL=$WHERE/USERS_R$ROUTER.dat
	cat $FNAME | awk '{print $3 "\t" $7 "\t" $8 "\t" $9 "\t" $10 "\t" $11}' | awk -F "_" '{print $1 "\t" $2}' | awk '{print $3 % 4 "\t" $4 "\t" $5 "\t" $6 "\t" $7}'  | grep "^$ROUTER"  > $FINAL
        ROUTER=$(( $ROUTER + 1 ))
done

rm $WHERE/USERS_MEAN.dat &> /dev/null

ROUTER=0
while [ $ROUTER -ne $NROUTERS ]
do
  cat $WHERE/USERS_R$ROUTER.dat | awk -f $SCRIPTS/USERS_AVG.awk >> $WHERE/USERS_MEAN.dat
  ROUTER=$(( $ROUTER + 1 ))
done
