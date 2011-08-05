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
package dk.frv.ais.country;

import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

/**
 * Class to represent a country by its assigned MID's (Maritime Identification Digits)
 * in addition to its ISO 3166 identification.
 * 
 * See {@link http://en.wikipedia.org/wiki/Maritime_Mobile_Service_Identity}
 * 
 */
public class MidCountry extends Country {

	private HashSet<Integer> mids = new HashSet<Integer>();

	protected MidCountry(String name, String twoLetter, String threeLetter, String number) {
		super(name, twoLetter, threeLetter, number);
	}

	public void addMid(int mid) {
		mids.add(mid);
	}

	public boolean matchMid(int mid) {
		return mids.contains(mid);
	}

	public HashSet<Integer> getMids() {
		return mids;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(", mids=");
		builder.append(StringUtils.join(mids.iterator(), ","));
		builder.append("]");
		return builder.toString();
	}
	
	

}
