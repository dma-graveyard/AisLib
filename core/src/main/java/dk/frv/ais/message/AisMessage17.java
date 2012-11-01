/* Copyright (c) 2012 Danish Maritime Authority
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
package dk.frv.ais.message;

import dk.frv.ais.binary.BinArray;
import dk.frv.ais.binary.SixbitEncoder;
import dk.frv.ais.binary.SixbitException;
import dk.frv.ais.sentence.Vdm;

/**
 * AIS message 17
 * 
 * GNSS broadcast binary message
 * 
 */
public class AisMessage17 extends AisMessage {

	private int spare1 = 0; // 2 bits: Spare. Should be set to zero. Reserved for future use
	private int lon; // 18 bits: Surveyed longitude
	private int lat; // 17 bits Surveyed latitude
	private int spare2 = 0; // 5 bits: Not used. Should be set to zero. Reserved for future use
	
	public AisMessage17() {
		super(7);
	}
	
	public AisMessage17(int num) {
		super(num);
	}

	public AisMessage17(Vdm vdm) throws AisMessageException, SixbitException {
		super(vdm);
		parse();
	}

	public void parse() throws AisMessageException, SixbitException {
		BinArray sixbit = vdm.getBinArray();
		if (sixbit.getLength() < 80 || sixbit.getLength() > 816) {
			throw new AisMessageException("Message " + msgId + " wrong length: " + sixbit.getLength());
		}
		super.parse(sixbit);
		this.spare1 = (int) sixbit.getVal(2);
		this.lon = (int) sixbit.getVal(18);
		this.lat = (int) sixbit.getVal(17);
		this.spare2 = (int) sixbit.getVal(5);
		
		// TODO the rest
		
	}

	@Override
	public SixbitEncoder getEncoded() {
		SixbitEncoder encoder = super.encode();
		
		// TODO
		
		return encoder;
	}

	public int getSpare1() {
		return spare1;
	}

	public void setSpare1(int spare1) {
		this.spare1 = spare1;
	}

	public int getLon() {
		return lon;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getSpare2() {
		return spare2;
	}

	public void setSpare2(int spare2) {
		this.spare2 = spare2;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(", spare1=");
		builder.append(spare1);
		builder.append(", lon=");
		builder.append(lon);
		builder.append(", lat=");
		builder.append(lat);
		builder.append(", spare2=");
		builder.append(spare2);
		return builder.toString();
	}
	
}
