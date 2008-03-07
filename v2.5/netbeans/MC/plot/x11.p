reset
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set yrange[0.0:1.0]
plot 'QoS.csv' using 7:10 with lines title "Grid Load", \
     'QoS.csv' using 7:11 with lines title "Quality Loss", \
     'QoS.csv' using 7:12 with lines title "Quality Loss Threeshold"  
unset output
