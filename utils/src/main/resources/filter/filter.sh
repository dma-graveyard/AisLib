#!/bin/sh

CP=.
for i in `ls ../lib/*.jar`
do
  CP=${CP}:${i}
done

echo $CP

java -cp $CP dk.frv.ais.utils.filter.AisFilter $@
