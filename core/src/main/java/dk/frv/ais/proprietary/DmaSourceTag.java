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
package dk.frv.ais.proprietary;

import dk.frv.ais.binary.SixbitException;
import dk.frv.ais.sentence.Sentence;
import dk.frv.ais.sentence.SentenceException;

/**
 * Class representing a DMA source tag
 * 
 * <source_name> : Name of source system
 * 
 * $PDMA,<source_name>*hh  
 *  
 */
public class DmaSourceTag extends Sentence implements IProprietaryTag {
	
	protected String sourceName;
	
	public DmaSourceTag() {
		talker = "P";
		formatter = "DMA";
		delimiter = "$";
	}
	
	@Override
	public String getSentence() {
		return getEncoded();
	}

	@Override
	public int parse(String line) throws SentenceException, SixbitException {
		
		return 0;
	}

	@Override
	public String getEncoded() {		
		super.encode();
		encodedFields.add(sourceName);
		return finalEncode();
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

}
