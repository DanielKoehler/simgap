#!/bin/sh
if [ -d $1 ]
then
ls -l $1 | awk '{print $8}' | xargs java -Xms3072m -Xmx3072m -cp MC.jar:libs/junit-4.1.jar:libs/GAP.jar:libs/GridSim.jar:libs/javacsv.jar:libs/simjava2.jar net.sf.gap.mc.QAGESA
else
java -Xms3072m -Xmx3072m -cp MC.jar:libs/junit-4.1.jar:libs/GAP.jar:libs/GridSim.jar:libs/javacsv.jar:libs/simjava2.jar net.sf.gap.mc.QAGESA $1
fi
