$ find . -name *.conf | awk -F "/" '{print $4 " " $2"_"$3}' | awk -F ".conf" '{print $1 $2}' | sed s/" "/_/g  | awk -F "_" '{print "cp "$3"/"$4"_"
$5"/"$1"_"$2".conf " $0".conf"}'  | bash
