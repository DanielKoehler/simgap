(cat $1.csv | awk -F ";" '{print $3 " " $8 " " $9 " " $10 }') > $1.dat
