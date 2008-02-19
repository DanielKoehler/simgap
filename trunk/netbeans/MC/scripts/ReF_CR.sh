(cat $1.csv | awk -F ";" '{print $3 " , " `echo $8 - 1000.0 | bc` " , " $9}') > $1.dat
