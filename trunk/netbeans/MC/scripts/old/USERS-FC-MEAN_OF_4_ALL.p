set terminal png
set output 'USERS-FC-MEAN-ALL.png'
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
plot 'linear/'.A.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) title "Linear - First Chunk", \
      'linear/'.B.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) title "Linear - First Chunk (BE)", \
      'scaled/'.A.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) title "Scaled - First Chunk", \
      'scaled/'.B.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) title "Scaled - First Chunk (BE)", \
      'top/'.A.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) title "Top - First Chunk", \
      'top/'.B.'/USERS_MEAN_OF_4.dat' using 2:xticlabels(1) title "Top - First Chunk (BE)" 
unset output