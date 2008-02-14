LOCAL_JRE=$1
LOCAL_MC=$2
REMOTEFILE_JRE=$(cat $LOCAL_JRE)
REMOTEFILE_MC=$(cat $LOCAL_MC)
LOCALFILE_JRE=$(basename $REMOTEFILE_JRE)
LOCALFILE_MC=$(basename $REMOTEFILE_MC)
LFC_HOST=infn-se-01.ct.pi2s2.it
echo "INFO:  Getting bzipped JRE to $PWD/$LOCALFILE_JRE from lfn:$REMOTEFILE_JRE at LFC_HOST=$LFC_HOST"
lcg-cp --vo cometa lfn:$REMOTEFILE_JRE file://$PWD/$LOCALFILE_JRE 
echo "INFO:  Extracting bzipped JRE $PWD/$LOCALFILE_JRE to $PWD/java"
rm -fR $PWD/java 
tar xvjf $LOCALFILE_JRE 
export PATH=$PWD/java/bin:$PATH
echo "INFO:  Getting zipped MC to $PWD/$LOCALFILE_MC from lfn:$REMOTEFILE_MC at LFC_HOST=$LFC_HOST"
lcg-cp --vo cometa lfn:$REMOTEFILE_MC file://$PWD/$LOCALFILE_MC 
echo "INFO:  Extracting zipped MC $PWD/$LOCALFILE_MC to $PWD/MC"
rm -fR $PWD/MC 
unzip $LOCALFILE_MC -d $PWD/MC 
cd MC
sh run.sh conf/QAGESA_64_16_MS.conf  512 512
