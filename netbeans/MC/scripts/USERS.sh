(cat $1.csv | awk -F ";" '{print $3 " " $8 " " $9 " " $10 }' | sort -k 2) > $1.dat
