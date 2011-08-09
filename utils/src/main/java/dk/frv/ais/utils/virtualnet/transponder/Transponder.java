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
package dk.frv.ais.utils.virtualnet.transponder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import dk.frv.ais.handler.IAisHandler;
import dk.frv.ais.message.AisMessage;
import dk.frv.ais.sentence.Sentence;
import dk.frv.ais.sentence.SentenceException;
import dk.frv.ais.utils.virtualnet.network.AisNetwork;

/**
 * Class that represents a virtual AIS transponder
 */
public class Transponder extends Thread implements IAisHandler {

	private static final Logger LOG = Logger.getLogger(Transponder.class);

	private long mmsi;
	private int tcpPort;
	private AisNetwork aisNetwork;
	private Socket clientSocket = null;
	private ServerSocket serverSocket = null;
	private OutputStream out = null;

	public Transponder(AisNetwork aisNetwork) {
		this.aisNetwork = aisNetwork;
		aisNetwork.addListener(this);
	}

	@Override
	public void run() {
		// Open server socket
		try {
			serverSocket = new ServerSocket(tcpPort);
		} catch (IOException e) {
			LOG.error("Failed to open server socket on port " + tcpPort + ": " + e.getMessage());
			return;
		}
		// Wait for connection
		while (true) {
			try {
				LOG.info("Transponder listening on port " + tcpPort);
				clientSocket = serverSocket.accept();
				out = clientSocket.getOutputStream();
				LOG.info("Transponder connected to " + clientSocket.getInetAddress());
			} catch (IOException e) {
				LOG.error("Subscriber server failed: " + e.getMessage());
				return;
			}
			try {
				readFromAI();
			} catch (IOException e) {
				clientSocket = null;
				out = null;
			}
		}

	}
	
	private void readFromAI() throws IOException {		
		BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			LOG.info("Read from client: " + line);
			// TODO handle read from AI
			
			// Parse ABM or BBM  (as VDM read and parse)
			
			// Make VDM
			
			// Send VDM to AisNetwork
			
			// Send ABK back to client socket
			
		}
		
		clientSocket.close();
		
	}

	@Override
	public void receive(AisMessage aisMessage) {
		// Maybe own
		boolean own = (aisMessage.getUserId() == mmsi); 

		// Convert to VDO or VDM
		StringBuilder buf = new StringBuilder();
		for (String sentence : aisMessage.getVdm().getOrgLines()) {
			try {
				buf.append(Sentence.convert(sentence, "AI", (own) ? "VDO" : "VDM") + "\r\n");
			} catch (SentenceException e) {
				LOG.error("Failed to convert sentence " + sentence);
				return;
			}
		}
		
		// Send to writer thread
		if (out != null) {
			try {
				out.write(buf.toString().getBytes());
			} catch (IOException e) {
				LOG.info("Failed to write to transponder client");
				try {
					clientSocket.close();
					out = null;
				} catch (IOException e1) { }
			}
		}

	}

	public long getMmsi() {
		return mmsi;
	}

	public void setMmsi(long mmsi) {
		this.mmsi = mmsi;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public AisNetwork getAisNetwork() {
		return aisNetwork;
	}

	public void setAisNetwork(AisNetwork aisNetwork) {
		this.aisNetwork = aisNetwork;
	}

}
