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

/**
 * Broadcast Binary and safety related Message as defined by IEC 61162
 * Sentence to encapsulate AIS message 8 and 14 for sending  
 */
public class Bbm extends SendSentence {
	
	public Bbm() {
		super();
		formatter = "BBM";
		channel = '0';
	}
	
	/**
	 * Get encoded sentence
	 */
	@Override
	public String getEncoded() {
		super.encode();
		return finalEncode();
	}

	/**
	 * Implemented parse method.
	 * See {@link EncapsulatedSentence}
	 */
	@Override
	public int parse(String line) throws SentenceException, SixbitException {
		
		// Do common parsing
		super.baseParse(line);
		
		// Check VDM / VDO
		if (!this.formatter.equals("BBM")) {
			throw new SentenceException("Not BBM sentence");
		}
		
		// Check that there at least 8 fields
		if (fields.length < 9) {
			throw new SentenceException("Sentence does not have at least 8 fields");
		}
		
		// Padding bits
		int padBits = Sentence.parseInt(fields[7]);

		// Six bit field
		this.sixbitString += fields[6];
		binArray.appendSixbit(fields[6], padBits);

		if (completePacket) {
			return 0;
		}

		return 1;
	}
	
}
