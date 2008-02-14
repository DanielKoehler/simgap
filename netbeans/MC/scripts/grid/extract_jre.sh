LOCAL_NAME=$1
REMOTEFILE=$(cat $LOCAL_NAME)
LOCALFILE=$(basename $REMOTEFILE)
LFC_HOST=infn-se-01.ct.pi2s2.it
echo "INFO:  Getting bzipped JRE to $PWD/$LOCALFILE from lfn:$REMOTEFILE at LFC_HOST=$LFC_HOST"
lcg-cp --vo cometa lfn:$REMOTEFILE file://$PWD/$LOCALFILE
echo "INFO:  Extracting bzipped JRE $PWD/$LOCALFILE to $PWD/java"
tar xvjf $LOCALFILE
export PATH=$PWD/java/bin:$PATH

