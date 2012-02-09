package dk.frv.ais.test.decode;

import org.apache.log4j.Logger;

import dk.frv.ais.handler.IAisHandler;
import dk.frv.ais.message.AisMessage;
import dk.frv.ais.message.AisPositionMessage;

public class PositionHandler implements IAisHandler {

	private static final Logger LOG = Logger.getLogger(PositionHandler.class);

	/**
	 * Method to handle incoming AIS messages
	 * 
	 * For debug out adjust log4j.xml level
	 */
	public void receive(AisMessage aisMessage) {
		// Ignore everything but position reports
		if (aisMessage.getMsgId() > 3) {
			return;
		}

		// Just consider the position part
		AisPositionMessage aisPosMessage = (AisPositionMessage) aisMessage;
		LOG.debug(aisPosMessage);

	}

}
