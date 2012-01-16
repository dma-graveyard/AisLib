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
import dk.frv.ais.message.AisMessage6;
import dk.frv.ais.message.AisMessage7;
import dk.frv.ais.message.AisPositionMessage;
import dk.frv.ais.sentence.Abk;
import dk.frv.ais.sentence.Abm;
import dk.frv.ais.sentence.Bbm;
import dk.frv.ais.sentence.Sentence;
import dk.frv.ais.sentence.SentenceException;
import dk.frv.ais.sentence.Vdm;
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
	private Abm abm = new Abm();
	private Bbm bbm = new Bbm();
	private Abk abk = new Abk();
	private TransponderOwnMessage ownMessage;

	public Transponder(AisNetwork aisNetwork) {
		this.aisNetwork = aisNetwork;
		ownMessage = new TransponderOwnMessage(this);
		aisNetwork.addListener(this);
	}

	@Override
	public void run() {
		// Start own message re-sender
		ownMessage.start();

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

			// Ignore everything else than sentences
			if (!Sentence.hasSentence(line)) {
				continue;
			}

			try {

				if (Abm.isAbm(line)) {
					int result = abm.parse(line);
					if (result == 0) {
						handleAbm();
					} else {
						continue;
					}
				}

				if (Bbm.isBbm(line)) {
					int result = bbm.parse(line);
					if (result == 0) {
						handleBbm();
					} else {
						continue;
					}
				}

				abm = new Abm();
				bbm = new Bbm();

			} catch (Exception e) {
				LOG.info("ABM or BBM failed: " + e.getMessage() + " line: " + line);
			}

		}

		clientSocket.close();

	}

	private void handleBbm() {
		LOG.info("Reveived complete BBM");
		abk = new Abk();
		abk.setChannel(bbm.getChannel());
		abk.setMsgId(bbm.getMsgId());
		abk.setSequence(bbm.getSequence());

		// Get AisMessage from Bbm
		try {
			Vdm vdm = bbm.makeVdm(mmsi, 0);
			aisNetwork.broadcast(vdm);
			abk.setResult(Abk.Result.BROADCAST_SENT);
		} catch (Exception e) {
			LOG.info("Error decoding BBM: " + e.getMessage());
			// Something must be wrong with Bbm
			abk.setResult(Abk.Result.COULD_NOT_BROADCAST);
		}

		sendAbk();
	}

	private void handleAbm() {
		LOG.info("Reveived complete ABM");
		abk = new Abk();
		abk.setChannel(abm.getChannel());
		abk.setMsgId(abm.getMsgId());
		abk.setSequence(abm.getSequence());
		abk.setDestination(abm.getDestination());

		// Get AisMessage from Abm
		try {
			Vdm vdm = abm.makeVdm(mmsi, 0, 0);
			aisNetwork.broadcast(vdm);
			abk.setResult(Abk.Result.ADDRESSED_SUCCESS);
		} catch (Exception e) {
			LOG.info("Error decoding ABM: " + e.getMessage());
			// Something must be wrong with Abm
			abk.setResult(Abk.Result.COULD_NOT_BROADCAST);
		}

		sendAbk();
	}

	private synchronized void sendAbk() {
		String encoded = abk.getEncoded() + "\r\n";
		LOG.info("Sending ABK: " + encoded);
		if (out != null) {
			try {
				out.write(encoded.getBytes());
			} catch (IOException e1) {
				try {
					clientSocket.close();
					out = null;
				} catch (IOException e2) {
				}
			}
		}
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

		// Maybe the transponder needs to send a binary acknowledge
		if (aisMessage.getMsgId() == 6) {
			AisMessage6 msg6 = (AisMessage6) aisMessage;
			if (msg6.getDestination() == mmsi) {
				sendBinAck(msg6);
			}
		}

		byte[] message = buf.toString().getBytes();
		// Save own (position) message
		if (own && aisMessage instanceof AisPositionMessage) {
			ownMessage.setOwnMessage(message);
		}
		sendData(message);

	}

	public synchronized void sendData(byte[] bytes) {
		// Send to writer thread
		if (out != null) {
			try {
				out.write(bytes);
				out.flush();
			} catch (IOException e) {
				LOG.info("Failed to write to transponder client");
				try {
					clientSocket.close();
					out = null;
				} catch (IOException e1) {
				}
			}
		}
	}

	private void sendBinAck(AisMessage6 msg6) {
		AisMessage7 msg7 = new AisMessage7();
		msg7.setUserId(mmsi);
		msg7.setDest1(msg6.getUserId());
		msg7.setSeq1(msg6.getSeqNum());
		LOG.info("Sending binary acknowledge: " + msg7);
		Vdm vdm = new Vdm();
		try {
			vdm.setMessageData(msg7);
			vdm.setSequence(msg6.getSeqNum());
			aisNetwork.broadcast(vdm);
		} catch (Exception e) {
			LOG.info("Failed to make binary ack: " + e.getMessage());
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

	public void setForceOwnInterval(int forceOwnInterval) {
		ownMessage.setForceInterval(forceOwnInterval);
	}

}
