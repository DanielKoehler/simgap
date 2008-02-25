set terminal png
set output 'Completed.png'
A='MS'
B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Number of Satisfied Requests"
plot A.'/completed.dat' using 2:3 title "Satisfied Play Requests" linecolor rgb 'green' with lines,  \
      B.'/completed.dat' using 2:3 title "Satisfied Play Requests (BE)" linecolor rgb 'red' with lines
unset output