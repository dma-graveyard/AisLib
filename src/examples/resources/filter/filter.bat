@echo OFF
set CLASSPATH=.;lib/*
@echo ON
java dk.frv.ais.examples.filter.AisFilter %*
