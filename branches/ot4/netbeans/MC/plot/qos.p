reset
A='results/'
#B='RMS'
set autoscale
unset log                              	# remove any log-scaling
unset label                            	# remove any previous labels
set xlabel "Simulation Time in seconds"
set yrange[0.0:1.0]
set terminal window
plot A.'QoS.csv' using 7:10 with lines title "Grid Load" linecolor rgb 'red' , \
      A.'QoS.csv' using 7:11 with lines title "Quality Loss" linecolor rgb 'green' , \
      A.'QoS.csv' using 7:12 with lines title "Quality Loss Threeshold" linecolor rgb 'blue'  
set terminal png
set output 'Requests.png'
plot A.'QoS.csv' using 7:10 with lines title "Grid Load" linecolor rgb 'red' , \
      A.'QoS.csv' using 7:11 with lines title "Quality Loss" linecolor rgb 'green' , \
      A.'QoS.csv' using 7:12 with lines title "Quality Loss Threeshold" linecolor rgb 'blue'  
unset output