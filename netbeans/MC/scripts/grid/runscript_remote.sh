REMOTEFILE=$(cat remotefile.txt)
LOCALFILE=$(basename $REMOTEFILE)
LFC_HOST=infn-se-01.ct.pi2s2.it
lcg-cp --vo cometa "$REMOTEFILE" "file:///$PWD/$LOCALFILE"
echo "extracting JVM..."
tar xvjf $LOCALFILE
export PATH=$PWD/java/bin:$PATH

