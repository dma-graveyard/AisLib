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
package dk.frv.ais.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dk.frv.ais.handler.IAisHandler;
import dk.frv.ais.message.AisMessage;
import dk.frv.ais.proprietary.IProprietaryFactory;
import dk.frv.ais.proprietary.IProprietaryTag;
import dk.frv.ais.sentence.Abk;
import dk.frv.ais.sentence.Sentence;
import dk.frv.ais.sentence.Vdm;

/**
 * Abstract base for classes reading from an AIS source. Also handles ABK and a
 * number of proprietary sentences.
 */
public abstract class AisReader extends Thread {

	private static final Logger LOG = Logger.getLogger(AisReader.class);

	public enum Status {
		CONNECTED, DISCONNECTED
	};

	/**
	 * List receivers for the AIS messages
	 */
	protected List<IAisHandler> handlers = new ArrayList<IAisHandler>();

	/**
	 * List of proprietary factories
	 */
	protected List<IProprietaryFactory> proprietaryFactories = new ArrayList<IProprietaryFactory>();

	/**
	 * A pool of sending threads. A sending thread handles the sending and
	 * reception of ABK message.
	 */
	protected SendThreadPool sendThreadPool = new SendThreadPool();

	/**
	 * A received VDO/VDM
	 */
	protected Vdm vdm = new Vdm();

	/**
	 * Possible proprietary tags for current VDM
	 */
	protected Deque<IProprietaryTag> tags = new ArrayDeque<IProprietaryTag>();

	/**
	 * Add an AIS handler
	 * 
	 * @param aisHandler
	 */
	public void registerHandler(IAisHandler aisHandler) {
		handlers.add(aisHandler);
	}

	/**
	 * Add a proprietary factory
	 * 
	 * @param proprietaryFactory
	 */
	public void addProprietaryFactory(IProprietaryFactory proprietaryFactory) {
		proprietaryFactories.add(proprietaryFactory);
	}

	/**
	 * Method to send addressed or broadcast AIS messages (ABM or BBM).
	 * 
	 * @param sendRequest
	 * @param resultListener
	 *            A class to handle the result when it is ready.
	 */
	public abstract void send(SendRequest sendRequest, ISendResultListener resultListener) throws SendException;

	/**
	 * Get the status of the connection, either connected or disconnected
	 * 
	 * @return status
	 */
	public abstract Status getStatus();

	/**
	 * The method to do the actual sending
	 * 
	 * @param sendRequest
	 * @param resultListener
	 * @param out
	 * @throws SendException
	 */
	protected void doSend(SendRequest sendRequest, ISendResultListener resultListener, OutputStream out) throws SendException {
		if (out == null) {
			throw new SendException("Not connected");
		}

		// Get sentences
		String[] sentences = sendRequest.createSentences();

		// Create and start thread
		SendThread sendThread = sendThreadPool.createSendThread(sendRequest, resultListener);

		// Write to out
		String str = StringUtils.join(sentences, "\r\n") + "\r\n";
		LOG.debug("Sending:\n" + str);
		try {
			out.write(str.getBytes());
		} catch (IOException e) {
			throw new SendException("Could not send AIS message: " + e.getMessage());
		}

		// Start send thread
		sendThread.start();
	}

	/**
	 * Handle a received line
	 * 
	 * @param line
	 */
	protected void handleLine(String line) {
		AisMessage message;

		// LOG.info("line: " + line);

		// Ignore everything else than sentences
		if (!Sentence.hasSentence(line)) {
			return;
		}

		// Check for ABK
		if (Abk.isAbk(line)) {
			Abk abk = new Abk();
			try {
				abk.parse(line);
				sendThreadPool.handleAbk(abk);
			} catch (Exception e) {
				LOG.error("Failed to parse ABK: " + line + ": " + e.getMessage());
			}
			return;
		}

		// Check if proprietary line
		if (Sentence.hasProprietarySentence(line)) {
			// Go through factories to find one that fits
			for (IProprietaryFactory factory : proprietaryFactories) {
				if (factory.match(line)) {
					tags.add(factory.getTag(line));
				}
			}
			return;
		}

		// Check for VDM
		if (!Vdm.isVdm(line)) {
			return;
		}

		try {
			int result = vdm.parse(line);
			// LOG.info("result = " + result);
			if (result == 0) {
				// Complete message
				message = AisMessage.getInstance(vdm);
				if (tags.size() > 0) {
					message.setTags(new ArrayDeque<IProprietaryTag>(tags));
				}
				for (IAisHandler aisHandler : handlers) {
					aisHandler.receive(message);
				}
			} else {
				// result = 1: Wait for more data
				return;
			}
		} catch (Exception e) {
			LOG.info("VDM failed: " + e.getMessage() + " line: " + line + " tag: " + ((tags.size() > 0) ? tags.peekLast() : "null"));
			// TODO Should this be handled more gracefully
		}

		vdm = new Vdm();
		tags.clear();

	}

	/**
	 * The main read loop
	 * 
	 * @param stream
	 *            the generic input stream to read from
	 * @throws IOException
	 */
	protected void readLoop(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;

		while ((line = reader.readLine()) != null) {
			handleLine(line);
		}

	}

}
