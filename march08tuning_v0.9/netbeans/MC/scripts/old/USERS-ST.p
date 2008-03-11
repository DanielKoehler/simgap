set terminal png
set output 'USERS-ST.png'
A='MS'
B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set ylabel "Streaming Time in seconds"
plot A.'/USERS_'.A.'_1.dat' using 2:4 title "Streaming Time" linecolor rgb 'green' with lines, \
      B.'/USERS_'.B.'_1.dat' using 2:4 title "Streaming Time (BE)" linecolor rgb 'red' with lines
unset output