AisLib
======

#### Java library for handling AIS messages ####

### Introduction ###

DaMSA AisLib is a Java library for handling AIS messages. This include

* Reading from AIS sources e.g. serial connection, TCP connection or file
* Handling of proprietary source tagging sentences
* Message filtering like doublet filtering and down sampling
* Decoding sentences and AIS messages
* Encoding sentences and AIS messages
* Sending AIS messages #6, #8, #12 and #14
* Handling application specific messages

The library contains test code and utility applications demonstrating
the use.

The project is separated into three parts:
  
* core     - The main AIS library
* test     - Various JUnit test code
* utils    - Various AIS utilities using AisLib


### Building ###

To build you will need

* JDK 1.6+ (http://java.sun.com/j2se/)
* Apache Ant 1.7+ (http://ant.apache.org)

To build everything
 
	ant
 
To run tests. All tests with filename *Test.java will be executed. 
Change build.xml to allow specific test targets.

	ant test

Make a distributable JAR file in project root

	ant dist
 
To make Javadoc

	ant javadoc
 

### Utils ###

The utilities source is located in utils/src

To build all utilities use 

	ant 
  
or

	ant utils

Each utility are placed under utils/. E.g. the filter application in
utils/filter. It can be started by first building and then using either
filter.sh or filter.bat on Linux and Windows respectively.

	ant
	cd utils/filter
	filter.bat -t localhost:4001 -d
  

### Contributing ###

You're encouraged to contribute to AisLib. Fork the code from 
[https://github.com/DaMSA/AisLib](https://github.com/DaMSA/AisLib) and submit pull requests.

### Versioning/naming ###

The version number/name is controlled in the core/build.xml file. Please use a 
name relating to the branch name. New official versions will only be made from
master branch. 

### License ###

This library is provided under the LGPL, version 3.

### Examples ###

#### Simplest examples ####

Reading from files or TCP connections is very simple with AisLib. In the example below messages
are read from a file.

```java
AisReader reader = new AisStreamReader(new FileInputStream("sentences.txt"));
reader.registerHandler(new IAisHandler() {			
	@Override
	public void receive(AisMessage aisMessage) {
		System.out.println("message id: " + aisMessage.getMsgId());	
	}
});
reader.start();
reader.join();
```

Reading using a TCP connection is just as easy

```java
AisTcpReader reader = new AisTcpReader("localhost", 4001);
reader.registerHandler(new IAisHandler() {			
	@Override
	public void receive(AisMessage aisMessage) {
		System.out.println("message id: " + aisMessage.getMsgId());		
	}
});
reader.start();
reader.join();
```

If the connection is broken the reader will try to reconnect after a certain amount of
time that can be set with:
```java
reader.setReconnectInterval(1000);
```

A read timeout can be defined for the reader. If no data is received within this period
the connection will be closed and a reconnect will be tried. 

