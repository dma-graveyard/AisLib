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
	 * Parse method. Will always return 0 as sentence will always be in a single line.
	 */
	@Override
	public int parse(String line) throws SentenceException {
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
		
		this.msgId = Integer.parseInt(fields[6]);
		
		// Six bit field
		// TODO
		
		return 0;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

}
