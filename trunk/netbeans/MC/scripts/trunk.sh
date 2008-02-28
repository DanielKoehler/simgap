cp $1/ReF_CR.dat $1/ReF_CR.dat.original
NN=$(N=$(cat $1/ReF_CR.dat  | wc | awk '{print $1 " - 1"}') ; echo $N | bc); cat $1/ReF_CR.dat | tail -n $NN > /tmp/ReF_CR.dat
rm $1/ReF_CR.dat &> /dev/null
mv /tmp/ReF_CR.dat $1/ReF_CR.dat
