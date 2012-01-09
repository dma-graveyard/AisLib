package dk.frv.ais.test.decode;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import dk.frv.ais.handler.IAisHandler;
import dk.frv.ais.message.AisMessage;
import dk.frv.ais.message.AisMessage4;
import dk.frv.ais.proprietary.IProprietarySourceTag;

/**
 * Example AIS message handler that saves MMSI for the base stations observed
 * 
 * For debug out adjust log4j.xml level
 * 
 * @author obo
 * 
 */
public class BaseReportHandler implements IAisHandler {

	private static final Logger LOG = Logger.getLogger(BaseReportHandler.class);

	private Set<Long> baseStations = new HashSet<Long>();
	private Set<Long> baseStationOrigins = new HashSet<Long>();

	public void receive(AisMessage aisMessage) {
		// Try to get proprietary source tag and evaluate base station origin
		IProprietarySourceTag sourceTag = aisMessage.getSourceTag();
		if (sourceTag != null) {
			baseStationOrigins.add(sourceTag.getBaseMmsi());
			LOG.debug("Observed base station origins: " + baseStationOrigins.size());
		}

		// Only handle base station reports
		if (aisMessage.getMsgId() != 4)
			return;

		// Cast to message 4 and save user id in hash set
		AisMessage4 msg4 = (AisMessage4) aisMessage;
		baseStations.add(msg4.getUserId());
		LOG.debug("Observed base stations: " + baseStations.size());
	}

	public Set<Long> getBaseStations() {
		return baseStations;
	}

	public Set<Long> getBaseStationOrigins() {
		return baseStationOrigins;
	}

}
