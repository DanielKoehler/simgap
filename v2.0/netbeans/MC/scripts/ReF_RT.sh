(cat $1.csv | grep "ReF_RT;1;"  | awk -F ";" '{print $3 " " $8 " " $9 " " $10}') > $1.dat
