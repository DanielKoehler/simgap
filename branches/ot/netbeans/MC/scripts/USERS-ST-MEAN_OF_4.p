set terminal png
set output 'USERS-ST-MEAN.png'
A='MS'
B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Attached to router"
set ylabel "Streaming Time in seconds"
set boxwidth 1.0 relative
set style histogram clustered gap 2
set style data histograms
set style fill solid 1.0
set yrange[0.0:]
plot A.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) linecolor rgb "green" title "Streaming Time", \
      B.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) linecolor rgb "red" title "Streaming Time (BE)" 
unset output