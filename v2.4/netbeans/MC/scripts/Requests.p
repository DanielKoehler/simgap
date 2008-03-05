set terminal png
set output 'Requests.png'
A='MS'
B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set ylabel "Number of Requests"
plot A.'/ReF_CR.dat' using 2:3 title "Concurrent Play Requests" linecolor rgb 'green' with lines,  \
      B.'/ReF_CR.dat' using 2:3 title "Concurrent Play Requests (BE)" linecolor rgb 'red' with lines
unset output