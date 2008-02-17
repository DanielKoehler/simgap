#!/bin/sh
java -Xms$2m -Xmx$3m -cp MC.jar:libs/junit-4.1.jar:libs/GAP.jar:libs/GridSim.jar:libs/javacsv.jar:libs/simjava2.jar net.sf.gap.mc.QAGESA $1
