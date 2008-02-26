set terminal png
set output 'USERS-RT.png'
A='MR'
B='RMR'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Response Time in seconds"
set xrange [1000.0:2000.0]
plot A.'/USERS_'.A.'_1.dat' using 2:4 linecolor rgb 'green' title "Response Time" with lines, \
      B.'/USERS_'.B.'_1.dat' using 2:4 linecolor rgb 'red' title "Response Time (BE)" with lines
unset output