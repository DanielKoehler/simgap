# Select results for USER 0
#!/bin/sh
(cat $1.csv | grep "_0;" | awk -F ";" '{print $3 " " $8 " " $9 " " $10 }' | sort -k 2) > $1.dat
