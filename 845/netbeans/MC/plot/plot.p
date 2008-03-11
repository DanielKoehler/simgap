pwd
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set terminal png
set output '../png/QoS.png'
set yrange[0.0:]
plot 'ReF_CR.csv' using 7:9 with lines title "Normalized Concurrent Requests" linecolor rgb 'black' , \
      'QoS.csv' using 7:11 with lines title "Quality Loss" linecolor rgb 'red' , \
      'QoS.csv' using 7:12 with lines title "Quality Loss Threeshold" linecolor rgb 'orange', \
      'USERS_Streaming.csv' using 8:3 title "" with lines linecolor rgb 'blue', \
      'USERS_Streaming.csv' using 8:28 with lines title "Normalized Intime Streams" linecolor rgb 'blue', \
      'USERS_Streaming.csv' using 8:29 with lines title "Normalized Outtime Streams" linecolor rgb 'violet', \
      'USERS_Streaming.csv' using 8:21 title "Normalized Streaming Time" linecolor rgb 'green'
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set ylabel "Normalized Streaming Time"
unset yrange
set yrange[0.0:]
set output '../png/ST.png'
plot 'USERS_R0.dat' using 2:5 title "R0", \
      'USERS_R1.dat' using 2:5 title "R1", \
      'USERS_R2.dat' using 2:5 title "R2", \
      'USERS_R3.dat' using 2:5 title "R3"
set xlabel "Attached to router"
set ylabel "Normalized Streaming Time"
set boxwidth 1.0 relative
set style histogram clustered gap 2
set style data histograms
set style fill solid 1.0
unset yrange
set yrange[0.0:]
set output '../png/ST_MEAN.png'
plot 'USERS_MEAN.dat' using 3:xticlabels(1) linecolor rgb "green" title "Normalized Streaming Time"
unset output
exit