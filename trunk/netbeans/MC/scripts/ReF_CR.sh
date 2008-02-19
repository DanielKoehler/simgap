(cat $1.csv | grep "ReF_CR;1;" | awk -F ";" '{print $3 " " $8 " " $9}') > $1.dat
