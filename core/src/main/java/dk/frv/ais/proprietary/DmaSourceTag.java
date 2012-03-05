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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
	
	private static final Logger LOG = Logger.getLogger(DmaSourceTag.class);
	
	protected String sourceName;
	private String sentence = null;
	
	public DmaSourceTag() {
		talker = "P";
		formatter = "DMA";
		delimiter = "$";
	}
	
	@Override
	public String getSentence() {
		if (sentence == null) {
			sentence = getEncoded(); 
		}
		return sentence;
	}

	@Override
	public int parse(String line) throws SentenceException, SixbitException {
		int start = line.indexOf("$PDMA,");
		if (start < 0) {
			throw new SentenceException("Error in DMA proprietary sentence: " + line);
		}
		line = line.substring(start);
		setSentence(line);
		
		// Remove checksum
		String dataLine = line.substring(0, line.indexOf("*"));
		
		String[] elems = StringUtils.splitByWholeSeparatorPreserveAllTokens(dataLine, ",");
		if (elems.length < 2) {
			LOG.error("Error in Gatehouse proprietary message: wrong number of fields " + elems.length + " in line: " + line);
			throw new SentenceException("Error in DMA proprietary sentence: " + line);
		}
		setSourceName(elems[1]);
		
		return 0;
	}

	@Override
	public String getEncoded() {		
		super.encode();
		encodedFields.add(sourceName);
		return finalEncode();
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DmaSourceTag [sourceName=");
		builder.append(sourceName);
		builder.append("]");
		return builder.toString();
	}

}
