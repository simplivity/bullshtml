IF "%1"=="" GOTO Continue

python googlecode_upload.py -s "bullshtml %1 - win32" -u junoyoon -p bullshtml target\bullshtml.exe
python googlecode_upload.py -s "bullshtml %1 - linux" -u junoyoon -p bullshtml target\bullshtml.tar.gz 

:Continue
