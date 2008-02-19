set yrange [0.0:30.0]
plot 'USERS_MS.dat' using 2:4 smooth csplines title "MS" with lines, \
      'USERS_RMS.dat' using 2:4 smooth csplines title "RMS" with lines, \
      'USERS_MR.dat' using 2:4 smooth csplines title "MR" with lines, \
      'USERS_RMR.dat' using 2:4 smooth csplines title "RMR" with lines, \
      'USERS_MF.dat' using 2:4 smooth csplines title "MF" with lines, \
      'USERS_RMF.dat' using 2:4 smooth csplines title "RMF" with lines
