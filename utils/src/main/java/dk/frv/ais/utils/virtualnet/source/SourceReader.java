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
package dk.frv.ais.utils.virtualnet.source;

import java.util.ArrayList;
import java.util.List;

import dk.frv.ais.filter.MessageDownSample;
import dk.frv.ais.handler.IAisHandler;
import dk.frv.ais.message.AisMessage;
import dk.frv.ais.reader.AisReader;
import dk.frv.ais.utils.virtualnet.network.AisNetwork;

/**
 * Class that sets up reading from multiple sources and handles the received
 * data
 */
public class SourceReader implements IAisHandler {

	private List<AisReader> readers = new ArrayList<AisReader>();
	private AisNetwork aisNetwork;
	private MessageDownSample downSampleFilter = new MessageDownSample(1);

	public SourceReader() {
		downSampleFilter.registerReceiver(this);
	}

	@Override
	public void receive(AisMessage aisMessage) {
		aisNetwork.broadcast(aisMessage);
	}

	public void addReader(AisReader aisReader) {
		aisReader.registerHandler(downSampleFilter);
		readers.add(aisReader);
	}

	/**
	 * Start all readers
	 */
	public void start() {
		for (AisReader aisReader : readers) {
			aisReader.start();
		}
	}

	public void setAisNetwork(AisNetwork aisNetwork) {
		this.aisNetwork = aisNetwork;
	}

	/**
	 * Set the down sampling rate
	 * 
	 * @param downSampleRate
	 */
	public void setDownsamplingRate(int downSampleRate) {
		downSampleFilter.setSamplingRate(downSampleRate);
	}

}
