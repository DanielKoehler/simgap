reset
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set yrange[0.0:1.0]
set terminal png
set output 'QoS.png'
plot 'QoS.csv' using 7:10 with lines title "Grid Load" linecolor rgb 'red' , \
      'QoS.csv' using 7:11 with lines title "Quality Loss" linecolor rgb 'green' , \
      'QoS.csv' using 7:12 with lines title "Quality Loss Threeshold" linecolor rgb 'blue'  
set autoscale
unset yrange
set yrange[0.0:]
set output 'ST.png'
plot 'USERS_R0.dat' using 2:4 with lines, \
      'USERS_R1.dat' using 2:4 with lines, \
      'USERS_R2.dat' using 2:4 with lines, \
      'USERS_R3.dat' using 2:4 with lines
set xlabel "Attached to router"
set ylabel "Streaming Time in seconds"
set boxwidth 1.0 relative
set style histogram clustered gap 2
set style data histograms
set style fill solid 1.0
set autoscale
unset yrange
set yrange[0.0:]
set output 'ST_MEAN.png'
plot 'USERS_MEAN.dat' using 2:xticlabels(1) linecolor rgb "green" title "Streaming Time"
set yrange[0.0:1.0]
set output 'ST_PMEAN.png'
plot 'USERS_MEAN.dat' using 3:xticlabels(1) linecolor rgb "green" title "% Streaming Time"
unset output