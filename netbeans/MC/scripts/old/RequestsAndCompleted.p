set terminal png
set output 'RequestsAndCompleted.png'
A='MS'
B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Number of Satisfied Requests"
plot A.'/completed.dat' using 2:3 title "Satisfied Play Requests" linecolor rgb 'green' lw 2 with lines,  \
      B.'/completed.dat' using 2:3 title "Satisfied Play Requests (BE)" linecolor rgb 'red' lw 2 with lines, \
      A.'/ReF_CR.dat' using 2:3 title "Concurrent Play Requests" linecolor rgb 'green' lw 1 with lines,  \
      B.'/ReF_CR.dat' using 2:3 title "Concurrent Play Requests (BE)" linecolor rgb 'red' lw 1 with lines
unset output