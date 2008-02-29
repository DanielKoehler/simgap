set terminal png
set output 'ReF-RT.png'
A='MS'
B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "ReF Response Time in seconds"
set title "Requests linearly grow from 0 to 64"
plot A.'/ReF_RT.dat' using 2:4 smooth csplines title "ReF Response Time" with lines, \
      B.'/ReF_RT.dat' using 2:4 smooth csplines title "ReF Response Time (BE)" with lines