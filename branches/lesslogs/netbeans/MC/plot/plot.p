pwd
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set terminal png
set output '../png/QoS.png'
set autoscale y
set yrange[0.0:]
plot 'ReF_CR.csv' using 7:9 with lines title "Normalized Concurrent Requests" linecolor rgb 'black' , \
      'QoS.csv' using 7:11 with lines title "Quality Loss" linecolor rgb 'red' , \
      'QoS.csv' using 7:12 with lines title "Quality Loss Threeshold" linecolor rgb 'orange', \
      'USERS_Streaming.csv' using 8:29 with lines title "Normalized Outtime Streams" linecolor rgb 'violet'
      
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set ylabel "Normalized Streaming Time"
set terminal png
set output '../png/ST.png'
set autoscale y
set yrange[0.0:]
plot 'USERS_Streaming.csv' using 8:21 title "Normalized Streaming Time" linecolor rgb 'green'
      
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Attached to router"
set ylabel "Time in seconds"
set boxwidth 1.0 relative
set style histogram clustered gap 2
set style data histograms
set style fill solid 1.0
unset xlabel
unset xrange
unset yrange
set autoscale
set yrange[0.0:]
set output '../png/FTRT_MEAN.png'
plot 'USERS_FT_MEAN.dat' using 3:xticlabels(1) linecolor rgb "blue" title "First Chunk Time", \
      'USERS_RT_MEAN.dat' using 3:xticlabels(1) linecolor rgb "red" title "Response Time"
set xlabel "Attached to router"
set ylabel "Normalized Streaming Time"
set boxwidth 1.0 relative
set style histogram clustered gap 2
set style data histograms
set style fill solid 1.0
unset xlabel
unset xrange
unset yrange
set autoscale
set yrange[0.0:]
set output '../png/ST_MEAN.png'
plot 'USERS_ST_MEAN.dat' using 3:xticlabels(1) linecolor rgb "green" title "Normalized Streaming Time"
unset output
exit