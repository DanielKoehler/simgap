set terminal png
set output 'Requests.png'
A='MS'
B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Number of Requests"
set xrange [1000.0:2000.0]
set yrange [0.0:]
set title "Requests linearly grow from 0 to 64"
plot A.'/ReF_CR.dat' using 2:3 smooth csplines title "Concurrent Play Requests" with lines
