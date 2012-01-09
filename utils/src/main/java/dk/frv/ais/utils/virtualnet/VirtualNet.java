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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import dk.frv.ais.utils.virtualnet.network.AisNetwork;
import dk.frv.ais.utils.virtualnet.source.SourceReader;
import dk.frv.ais.utils.virtualnet.transponder.Transponder;

/**
 * Application to set up a virtual AIS network with sources and virtual
 * transponders. See url TODO for description
 * 
 */
public class VirtualNet {

	private static Logger LOG;

	private static Settings settings = new Settings();
	private static AisNetwork aisNetwork = new AisNetwork();
	private static SourceReader sourceReader = new SourceReader();
	private static List<Transponder> transponders = new ArrayList<Transponder>();

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

		// Start reading from sources
		sourceReader.setAisNetwork(aisNetwork);
		sourceReader.start();

		// Start transponders
		for (Transponder transponder : transponders) {
			transponder.start();
		}

		// Maintanaince loop
		while (true) {
			Thread.sleep(10000);
		}

	}

	public static Settings getSettings() {
		return settings;
	}

	public static void setSourceReader(SourceReader sourceReader) {
		VirtualNet.sourceReader = sourceReader;
	}

	public static AisNetwork getAisNetwork() {
		return aisNetwork;
	}

	public static SourceReader getSourceReader() {
		return sourceReader;
	}

	public static List<Transponder> getTransponders() {
		return transponders;
	}

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			LOG.error("Sleep failed: " + e.getMessage());
		}
	}

}
