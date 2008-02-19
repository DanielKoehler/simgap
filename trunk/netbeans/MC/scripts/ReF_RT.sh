WHICH=$1
FILE=$2
(cat $FILE.csv | grep "ReF_RT;$WHICH;"  | awk -F ";" '{print $3 " " $8 " " $9 " " $10}') > $FILE.dat
