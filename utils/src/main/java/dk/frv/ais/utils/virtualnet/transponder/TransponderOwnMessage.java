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

import org.apache.log4j.Logger;

import dk.frv.ais.utils.virtualnet.VirtualNet;

/**
 * Utility class to hold latest OWN message and resend with certain interval
 */
public class TransponderOwnMessage extends Thread {

	private static final Logger LOG = Logger.getLogger(TransponderOwnMessage.class);

	private static final long MESSAGE_MAX_AGE = 20 * 60 * 1000; // 20 minutes

	private long lastReceived = 0L;
	private byte[] ownMessage = null;
	private int forceInterval = 0;
	private Transponder transponder = null;

	public TransponderOwnMessage(Transponder transponder) {
		this.transponder = transponder;
	}

	@Override
	public void run() {
		// Should own message re-sending be forced
		if (forceInterval == 0) {
			return;
		}

		// Wait for everything to start up
		VirtualNet.sleep(5000);

		// Enter re-send loop
		while (true) {
			VirtualNet.sleep(forceInterval * 1000);
			reSend();
		}
	}

	private synchronized void reSend() {
		if (ownMessage == null) {
			return;
		}

		// Determine last send elapsed
		long elapsed = System.currentTimeMillis() - lastReceived;
		// Do not send if already sent with interval
		if (elapsed < forceInterval * 1000) {
			return;
		}
		// Send if not too old
		if (elapsed < MESSAGE_MAX_AGE) {
			LOG.info("Re-sending own message for " + transponder.getMmsi());
			transponder.sendData(ownMessage);
		}

	}

	public synchronized void setOwnMessage(byte[] ownMessage) {
		this.ownMessage = ownMessage;
		this.lastReceived = System.currentTimeMillis();
	}

	public void setForceInterval(int forceInterval) {
		this.forceInterval = forceInterval;
	}

}
