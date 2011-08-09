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
package dk.frv.ais.sentence;

import dk.frv.ais.binary.SixbitException;
import dk.frv.ais.message.AisMessage;
import dk.frv.ais.message.AisMessage12;
import dk.frv.ais.message.AisMessage6;

/**
 * Addressed Binary and safety related Message as defined by IEC 61162
 * Sentence to encapsulate AIS message 6 and 12 for sending  
 */
public class Abm extends SendSentence {

	private int destination;

	public Abm() {
		super();
		formatter = "ABM";
		channel = '0';
	}

	/**
	 * Get encoded sentence
	 */
	@Override
	public String getEncoded() {
		super.encode();
		// Add destination
		encodedFields.add(4, Integer.toString(destination));
		return finalEncode();
	}

	/**
	 * Implemented parse method.
	 * See {@link EncapsulatedSentence}
	 * @throws SentenceException
	 * @throws SixbitException 
	 */
	@Override
	public int parse(String line) throws SentenceException, SixbitException {
		// Do common parsing
		super.baseParse(line);

		// Check ABM
		if (!this.formatter.equals("ABM")) {
			throw new SentenceException("Not ABM sentence");
		}
		
		// Check that there at least 9 fields
		if (fields.length < 9) {
			throw new SentenceException("Sentence does not have at least 9 fields");
		}
		
		this.destination = Integer.parseInt(fields[4]);
		
		// Channel, relaxed may be null
		if (fields[5].length() > 0) {
			this.channel = fields[5].charAt(0);
		} else {
			this.channel = 0;
		}
		
		// Message id
		this.msgId = Integer.parseInt(fields[6]);
		
		// Padding bits
		int padBits = Sentence.parseInt(fields[8]);
		
		// Six bit field
		this.sixbitString += fields[7];
		binArray.appendSixbit(fields[7], padBits);
		
		if (completePacket) {
			return 0;
		}

		return 1;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}
		
	/**
	 * Get AIS message 6 or 12 for this ABM  
	 * @return
	 */
	public AisMessage getAisMessage() {
		// TODO maybe make special encoders in msg6 and msg12 instead
		// They just need to take bin/text part from here instead
		// and pad bits
		// Problem that we need to parse application specific message
		// to be able to make it into a VDM
		
		
		AisMessage aisMessage = null;
		if (msgId == 12) {
			// Decode text part and 
			AisMessage12 msg12 = new AisMessage12();
			msg12.setDestination(getDestination());
			msg12.setMessage("");
			// Correct ?
			msg12.setSeqNum(getSequence());
			aisMessage = msg12;
		} else if (msgId == 6) {
			AisMessage6 msg6 = new AisMessage6();
			// TODO
		}
	
		return aisMessage;
	}

}
