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
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dk.frv.ais.reader.AisSerialReader;
import dk.frv.ais.reader.AisTcpReader;
import dk.frv.ais.utils.virtualnet.transponder.Transponder;

/**
 * Settings class to load settings and setup project entities
 */
public class Settings {

	private static final Logger LOG = Logger.getLogger(Settings.class);

	private Properties props;

	public Settings() {
	}

	public void load(String filename) throws IOException {
		props = new Properties();
		URL url = ClassLoader.getSystemResource(filename);
		if (url == null) {
			throw new IOException("Could not find properties file: " + filename);
		}
		props.load(url.openStream());

		// Iterate throgh serial sources
		String serialSourcesStr = props.getProperty("serial_sources", "");
		for (String sourceDescription : StringUtils.split(serialSourcesStr, ",")) {
			AisSerialReader aisSerialReader = new AisSerialReader();
			aisSerialReader.setPortName(props.getProperty("serial_port." + sourceDescription, ""));
			aisSerialReader.setPortSpeed(getInt("serial_speed." + sourceDescription, "38400"));
			aisSerialReader.setDataBits(getInt("serial_data_bits." + sourceDescription, "8"));
			aisSerialReader.setStopBits(getInt("serial_stop_bits." + sourceDescription, "1"));
			aisSerialReader.setParity(getInt("serial_parity." + sourceDescription, "0"));
			// Find port
			try {
				aisSerialReader.findPort();
			} catch (IOException e) {
				LOG.error("Failed to find serial port " + aisSerialReader.getPortName());
				continue;
			}
			LOG.info("Adding serial source " + aisSerialReader.getPortName() + " (" + sourceDescription + ")");
			VirtualNet.getSourceReader().addReader(aisSerialReader);
		}

		// Iterate through TCP sources
		String tcpSourcesStr = props.getProperty("tcp_sources", "");
		for (String tcpSourceName : StringUtils.split(tcpSourcesStr, ",")) {
			AisTcpReader aisTcpReader = new AisTcpReader();
			aisTcpReader.setHostname(props.getProperty("tcp_source_host." + tcpSourceName, "localhost"));
			aisTcpReader.setPort(getInt("tcp_source." + tcpSourceName, "4001"));
			LOG.info("Adding TCP source " + tcpSourceName + " " + aisTcpReader.getHostname() + ":" + aisTcpReader.getPort());
			VirtualNet.getSourceReader().addReader(aisTcpReader);
		}

		// Iterate through transponders
		String transpondersStr = props.getProperty("transponders", "");
		for (String transponderName : StringUtils.split(transpondersStr, ",")) {
			Transponder transponder = new Transponder(VirtualNet.getAisNetwork());
			transponder.setMmsi(getInt("transponder_mmsi." + transponderName, "0"));
			transponder.setTcpPort(getInt("transponder_tcp_port." + transponderName, "0"));
			transponder.setForceOwnInterval(getInt("transponder_own_message_force." + transponderName, "0"));
			LOG.info("Adding transponder " + transponderName + " mmsi " + transponder.getMmsi() + " port "
					+ transponder.getTcpPort());
			VirtualNet.getTransponders().add(transponder);
		}

	}

	private int getInt(String key, String defaultValue) {
		String val = props.getProperty(key, defaultValue);
		return Integer.parseInt(val);
	}

}
