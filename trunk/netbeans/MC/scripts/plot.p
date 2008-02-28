A='MS'
B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Measure in seconds"
set xrange [1000.0:2000.0]
#set yrange [0:30]
plot A.'/USERS_'.A.'.dat' using 2:4 smooth csplines title "ST" with lines, \
      B.'/USERS_'.B.'.dat' using 2:4 smooth csplines title "ST (BE)" with lines