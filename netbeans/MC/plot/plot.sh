# Select results for USER attached to router I with I=0..NROUTERS-1 where NROUTERS is number of routers
#!/bin/sh
SCRIPTS=$1
WHERE=$2
FILE=USERS_Streaming
FNAMECSV=$WHERE/$FILE.csv
FNAME=$2/USERS.dat
NROUTERS=$3

rm $2/*.dat

NL=$(cat $FNAMECSV | wc | awk '{print $1 "-1"}' | bc) 
cat $FNAMECSV | tail -n $NL > $FNAME

ROUTER=0
while [ $ROUTER -ne $NROUTERS ]
do
	FT=$WHERE/USERS_FT_R$ROUTER.dat
	RT=$WHERE/USERS_RT_R$ROUTER.dat
	ST=$WHERE/USERS_ST_R$ROUTER.dat
	cat $FNAME | awk '{print $7 "\t" $11 "\t" $13 "\t" $19 "\t" $19}' | awk -F "_" '{print $1 "\t" $2}' | awk '{print $2 % 4 "\t" $3 "\t" $4 "\t" $5 "\t" $6}'  | grep "^$ROUTER"  > $FT
	cat $FNAME | awk '{print $7 "\t" $11 "\t" $12 "\t" $18 "\t" $18}' | awk -F "_" '{print $1 "\t" $2}' | awk '{print $2 % 4 "\t" $3 "\t" $4 "\t" $5 "\t" $6}'  | grep "^$ROUTER"  > $RT
	cat $FNAME | awk '{print $7 "\t" $13 "\t" $14 "\t" $20 "\t" $21}' | awk -F "_" '{print $1 "\t" $2}' | awk '{print $2 % 4 "\t" $3 "\t" $4 "\t" $5 "\t" $6}'  | grep "^$ROUTER"  > $ST
        ROUTER=$(( $ROUTER + 1 ))
done

rm $WHERE/USERS_MEAN.dat &> /dev/null

ROUTER=0
while [ $ROUTER -ne $NROUTERS ]
do
  cat $WHERE/USERS_ST_R$ROUTER.dat | awk -f $SCRIPTS/USERS_AVG.awk >> $WHERE/USERS_ST_MEAN.dat
  cat $WHERE/USERS_RT_R$ROUTER.dat | awk -f $SCRIPTS/USERS_AVG.awk >> $WHERE/USERS_RT_MEAN.dat
  cat $WHERE/USERS_FT_R$ROUTER.dat | awk -f $SCRIPTS/USERS_AVG.awk >> $WHERE/USERS_FT_MEAN.dat
  ROUTER=$(( $ROUTER + 1 ))
done
echo "set xrange[$5:$6]" > png/temp.p
cat $SCRIPTS/plot.p >> png/temp.p 
cd $WHERE
gnuplot  ../png/temp.p #2> /dev/null
rm ../png/temp.p


