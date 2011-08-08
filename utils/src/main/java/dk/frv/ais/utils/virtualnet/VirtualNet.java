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
package dk.frv.ais.utils.virtualnet;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Application to set up a virtual AIS network with sources
 * and virtual transponders. See url TODO for description 
 * 
 */
public class VirtualNet {
	
	private static Logger LOG;
	
	private static Settings settings = new Settings();

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		DOMConfigurator.configure("log4j.xml");
		LOG = Logger.getLogger(VirtualNet.class);
		LOG.info("Starting VirtualNet");
		
		// Load configuration
		String settingsFile = "virtualnet.properties";
		if (args.length > 0) {
			settingsFile = args[0];
		}
		try {
			settings.load(settingsFile);
		} catch (IOException e) {
			LOG.error("Failed to load settings: " + e.getMessage());
			System.exit(-1);
		}
		
		// Maintanaince loop
		while (true) {
			Thread.sleep(10000);
		}

	}

}
