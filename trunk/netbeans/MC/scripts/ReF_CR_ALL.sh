cat ReF_CR.csv  | awk -F ";" '{print $3 " " $8 " " $9}' | sort -k 2 > ReF_CR_ALL.dat
