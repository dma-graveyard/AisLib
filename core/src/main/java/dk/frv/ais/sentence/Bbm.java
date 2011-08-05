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
	 * Parse method. Will always return 0 as sentence will always be in a single line.
	 */
	@Override
	public int parse(String line) {
		// TODO
		return 0;
	}
	
}
