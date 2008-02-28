set terminal png
set output 'USERS-FC.png'
A='MF'
B='RMF'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set ylabel "Time for First Chunk in seconds"
plot A.'/USERS_'.A.'_1.dat' using 2:4 title "First Chunk" linecolor rgb 'green' with lines, \
      B.'/USERS_'.B.'_1.dat' using 2:4 title "First Chunk (BE)" linecolor rgb 'red' with lines
unset output      