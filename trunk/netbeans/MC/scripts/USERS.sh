(cat $1.csv  | grep "USERS_[A-Z][A-Z];1;" | awk -F ";" '{print $3 " " $8 " " $9 " " $10 }') > $1.dat
