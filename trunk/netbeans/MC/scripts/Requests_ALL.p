set terminal png
set output 'Requests_ALL.png'
A='MS'
B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Number of Requests"
plot A.'/ReF_CR_ALL.dat' using 2:3 title "Concurrent Play Requests" with lines,  \
      B.'/ReF_CR_ALL.dat' using 2:3 title "Concurrent Play Requests (BE)" with lines
unset output