echo | mail -s "`date` - started" giovanni.novelli@gmail.com
sh run.sh conf/QAGESA_64_16_MR.conf
sh run.sh conf/QAGESA_64_16_MS.conf
sh run.sh conf/QAGESA_64_16_RMF.conf
sh run.sh conf/QAGESA_64_16_RMR.conf
sh run.sh conf/QAGESA_64_16_RMS.conf
sh run.sh conf/QAGESAXML_64_16_MF.conf
sh run.sh conf/QAGESAXML_64_16_MR.conf
sh run.sh conf/QAGESAXML_64_16_MS.conf
sh run.sh conf/QAGESAXML_64_16_RMF.conf
sh run.sh conf/QAGESAXML_64_16_RMR.conf
sh run.sh conf/QAGESAXML_64_16_RMS.conf
sh run.sh conf/QAGESAXML_64_16_MF.conf
sh run.sh conf/QAGESAXML_64_16_MR.conf
sh run.sh conf/QAGESAXML_64_16_MS.conf
sh run.sh conf/QAGESAXML_64_16_RMF.conf
sh run.sh conf/QAGESAXML_64_16_RMR.conf
sh run.sh conf/QAGESAXML_64_16_RMS.conf
echo | mail -s "`date` - finished" giovanni.novelli@gmail.com

