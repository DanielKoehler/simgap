set terminal png
set output 'USERS-ST-RMS.png'
A='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Streaming Time in seconds"
#set xrange [1000.0:1400.0]
plot A.'/USERS_0.dat' using 2:4 title "Streaming Time (R0)" with lines, \
      A.'/USERS_1.dat' using 2:4 title "Streaming Time (R1)" with lines, \
      A.'/USERS_2.dat' using 2:4 title "Streaming Time (R2)" with lines, \
      A.'/USERS_3.dat' using 2:4 title "Streaming Time (R3)" with lines
unset output