# Select first replication results
#!/bin/sh
(cat $1.csv | grep ";1;" | grep "_0;" | awk -F ";" '{print $3 " " $8 " " $9 " " $10 }' | sort -k 2) > $1_1_0.dat
