pwd
reset
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set terminal png
set output '../png/QoS.png'
set yrange[0.0:3.0]
plot 'QoS.csv' using 7:10 with lines title "Transcoding Load" linecolor rgb 'black' , \
      'QoS.csv' using 7:11 with lines title "Quality Loss" linecolor rgb 'red' , \
      'QoS.csv' using 7:12 with lines title "Quality Loss Threeshold" linecolor rgb 'orange', \
      'USERS_Streaming.csv' using 8:21 title "Normalized Streaming Time" linecolor rgb 'green', \
      'USERS_Streaming.csv' using 8:28 with lines title "Normalized Intime Streams" linecolor rgb 'blue', \
      'USERS_Streaming.csv' using 8:29 with lines title "Normalized Outtime Streams" linecolor rgb 'violet'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
unset yrange
set yrange[0.0:]
set terminal png
set output '../png/QoS_ST.png'
plot 'QoS.csv' using 7:10 with lines title "Transcoding Load" linecolor rgb 'black , \
      'QoS.csv' using 7:11 with lines title "Quality Loss" linecolor rgb 'red' , \
      'QoS.csv' using 7:12 with lines title "Quality Loss Threeshold" linecolor rgb 'orange', \
      'USERS.dat' using 9:11 title "Streaming Time %" linecolor rgb 'green'
set autoscale
unset yrange
set yrange[0.0:]
set output '../png/ST.png'
plot 'USERS_R0.dat' using 2:4, \
      'USERS_R1.dat' using 2:4, \
      'USERS_R2.dat' using 2:4, \
      'USERS_R3.dat' using 2:4
set xlabel "Attached to router"
set ylabel "Streaming Time in seconds"
set boxwidth 1.0 relative
set style histogram clustered gap 2
set style data histograms
set style fill solid 1.0
set autoscale
unset yrange
set yrange[0.0:]
set output '../png/ST_MEAN.png'
plot 'USERS_MEAN.dat' using 2:xticlabels(1) linecolor rgb "green" title "Streaming Time"
set yrange[0.0:1.0]
set output '../png/ST_PMEAN.png'
plot 'USERS_MEAN.dat' using 3:xticlabels(1) linecolor rgb "green" title "% Streaming Time"
unset output
exit