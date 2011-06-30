#!/bin/sh

CP=.
for i in `ls lib/*.jar`
do
  CP=${CP}:${i}
done

java -cp $CP dk.frv.ais.examples.dumper.AisDumper $@
