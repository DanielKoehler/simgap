unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time"
set ylabel "Measure in seconds"
set yrange [0.0:15]
plot 'ReF_RT.dat' using 2:4 smooth csplines title "ReF" with lines, \
      'USERS_MS.dat' using 2:4 smooth csplines title "ST" with lines, \
      'USERS_RMS.dat' using 2:4 smooth csplines title "ST (BE)" with lines, \
      'USERS_MR.dat' using 2:4 smooth csplines title "RT" with lines, \
      'USERS_RMR.dat' using 2:4 smooth csplines title "RT (BE)" with lines, \
      'USERS_MF.dat' using 2:4 smooth csplines title "FC" with lines, \
      'USERS_RMF.dat' using 2:4 smooth csplines title "FC (BE)" with lines
