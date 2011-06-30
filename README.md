AisLib
======

#### Java library for handling AIS messages ####

### Introduction ###

DaMSA AisLib is a Java library for handling AIS messages. This include

* Reading from AIS sources e.g. serial connection, TCP connection or file
* Decoding sentences and AIS messages
* Encoding sentences and AIS messages
* Sending AIS messages #6, #8, #12 and #14
* Handling application specific messages

The library contains test code and example applications.

The project is separated into three parts:
  
* main     - The main AIS library
* test     - Various JUnit test code
* examples - Example applications


### Building ###

To build you will need

* JDK 1.6+ (http://java.sun.com/j2se/)
* Apache Ant 1.7+ (http://ant.apache.org)

To build everything
 
	ant
 
To run tests. All tests with filename *Test.java will be executed. 
Change build.xml to allow specific test targets.

	ant test

Make a distributable JAR file

	ant dist
 
To make Javadoc

	ant javadoc
 

### Examples ###

The examples source are placed under src/examples/java

To build all example applications use 

	ant 
  
or

	ant examples
  
The example applications are installed in examples/. Installation of the
example applications are controlled in build-examples.xml.

The dumper application can be started by first building and then going to
the examples/dumper and use either dumper.sh or dumper.bat on Linux and
Windows respectively.

	ant
	cd examples/dumper
	sh dumper.sh -t localhost:4001
  
The filter application can be started by first building and then going to
the examples/filter and use either filter.sh or filter.bat on Linux and
Windows respectively.

	ant
	cd examples/dumper
	sh dumper.sh -t localhost:4001
  

### Contributing ###

TODO more on how to contribute etc

You're encouraged to contribute to AisLib. Fork the code from 
[github.com/TODO](https://github.com/TODO) and submit pull requests.

### Versioning/naming ###

The version number/name is controlled in the build.xml file. Please use a 
name relating to the branch name. New official versions will only be made from
master branch. 

### License ###

This library is provided under the LGPL, version 3.