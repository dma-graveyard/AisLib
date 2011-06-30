@echo ON
set CLASSPATH=.;lib/*
java -cp %CLASSPATH% dk.frv.ais.examples.sender.SrmSend %*
