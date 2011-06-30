/* Copyright (c) 2011 Danish Maritime Safety Administration
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.frv.ais.examples.dumper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import dk.frv.ais.proprietary.GatehouseFactory;
import dk.frv.ais.reader.AisReader;
import dk.frv.ais.reader.AisStreamReader;
import dk.frv.ais.reader.AisTcpReader;

/**
 * Simple application to dump all AIS messages in a Human readable format
 * 
 * Usage:
 * 
 * AisDumper <-t|-f> <filename/host:port> 
 *   -t TCP connection to host:port 
 *   -f Read from file filename
 * 
 * @throws FileNotFoundException
 * @throws InterruptedException
 * 
 */
public class AisDumper {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		// Read command line arguments
		String filename = null;
		String hostPort = null;
		if (args.length < 2) {
			usage();
			System.exit(1);
		}
		int i = 0;
		while (i < args.length) {
			if (args[i].indexOf("-t") >= 0) {
				hostPort = args[++i];
			} else if (args[i].indexOf("-f") >= 0) {
				filename = args[++i];
			}
			i++;
		}
		if (filename == null && hostPort == null) {
			usage();
			System.exit(1);
		}

		// Use TCP or file
		AisReader aisReader;
		if (filename != null) {
			aisReader = new AisStreamReader(new FileInputStream(filename));
		} else {
			aisReader = new AisTcpReader(hostPort);
		}

		// Message handler
		MessageHandler messageHandler = new MessageHandler();

		// Register handler
		aisReader.registerHandler(messageHandler);

		// Register proprietary handler (optional)
		aisReader.addProprietaryFactory(new GatehouseFactory());

		// Start reader thread
		aisReader.start();

		// Wait for thread to finish
		aisReader.join();

	}

	public static void usage() {
		System.out.println("Usage: AisDumper <-t|-f> <filename/host:port>");
		System.out.println("\t -t TCP connection to host:port");
		System.out.println("\t -f Read from file filename");
	}

}
