(cat $1.csv | grep "ReF_PR;1;" | awk -F ";" '{print $3 " " $8 " " $9}' | grep -v " 0.0 " | awk '{print $1 " " $2-1000.0 " " $3}' | sort -u -n -k 2 ) > $1.dat
