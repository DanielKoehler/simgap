set terminal png
set output 'USERS-FC.png'
A='MF'
B='RMF'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Time for First Chunk in seconds"
set xrange [1000.0:2000.0]
set title "Requests linearly grow from 0 to 64"
plot A.'/USERS_'.A.'.dat' using 2:4 smooth csplines title "First Chunk" with lines, \
      B.'/USERS_'.B.'.dat' using 2:4 smooth csplines title "First Chunk (BE)" with lines
unset output      