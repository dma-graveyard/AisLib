@echo OFF
set CLASSPATH=.;lib/*
@echo ON
java -cp %CLASSPATH% dk.frv.ais.examples.dumper.AisDumper %1 %2 %3 %4
