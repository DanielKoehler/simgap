set terminal png
set output 'USERS-RT.png'
A='MR'
B='RMR'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set ylabel "Response Time in seconds"
plot A.'/USERS_'.A.'_1.dat' using 2:4 linecolor rgb 'green' title "Response Time" with lines, \
      B.'/USERS_'.B.'_1.dat' using 2:4 linecolor rgb 'red' title "Response Time (BE)" with lines
unset output