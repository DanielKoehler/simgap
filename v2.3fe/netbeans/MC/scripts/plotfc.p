set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Requests"
set y2label "Measure in seconds"
set xrange [1000.0:2000.0]
set yrange [0:64]
set y2range [0.0:30.0]
plot 'ReF_CR.dat' using 2:3 smooth csplines title "ReF" with lines axis x1y1, \
      'USERS_MF.dat' using 2:4 smooth csplines title "FC" with lines axis x1y2, \
      'USERS_RMF.dat' using 2:4 smooth csplines title "FC (BE)" with lines axis x1y2 
