sed s/QAGESA.conf/QAGESA_128_MF.conf/g run2g.jdl > QAGESA_128_MF.conf.jdl; edg-job-submit --vo cometa -o $HOME/JID_12810k QAGESA_128_MF.conf.jdl
sed s/QAGESA.conf/QAGESA_128_MR.conf/g run2g.jdl > QAGESA_128_MR.conf.jdl; edg-job-submit --vo cometa -o $HOME/JID_12810k QAGESA_128_MR.conf.jdl
sed s/QAGESA.conf/QAGESA_128_MS.conf/g run2g.jdl > QAGESA_128_MS.conf.jdl; edg-job-submit --vo cometa -o $HOME/JID_12810k QAGESA_128_MS.conf.jdl
sed s/QAGESA.conf/QAGESA_128_RMF.conf/g run2g.jdl > QAGESA_128_RMF.conf.jdl; edg-job-submit --vo cometa -o $HOME/JID_12810k QAGESA_128_RMF.conf.jdl
sed s/QAGESA.conf/QAGESA_128_RMR.conf/g run2g.jdl > QAGESA_128_RMR.conf.jdl; edg-job-submit --vo cometa -o $HOME/JID_12810k QAGESA_128_RMR.conf.jdl
sed s/QAGESA.conf/QAGESA_128_RMS.conf/g run2g.jdl > QAGESA_128_RMS.conf.jdl; edg-job-submit --vo cometa -o $HOME/JID_12810k QAGESA_128_RMS.conf.jdl
