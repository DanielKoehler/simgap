WHICH=$1
FILE=$2
(cat $FILE.csv  | grep "USERS_[A-Z][A-Z];$WHICH;" | grep "_0;"  | awk -F ";" '{print $3 " " $8 " " $9 " " $10 }') > $FILE.dat
