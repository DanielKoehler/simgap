set terminal png
set output 'USERS-FC-MEAN.png'
A='MF'
B='RMF'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Attached to router"
set ylabel "Time for First Chunk in seconds"
set boxwidth 1.0 relative
set style histogram clustered gap 2
set style data histograms
set style fill solid 1.0
set yrange[0.0:]
plot A.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) linecolor rgb "green" title "First Chunk", \
      B.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) linecolor rgb "red" title "First Chunk (BE)" 
unset output