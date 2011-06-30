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

/**
 * Class representing an ISO 3166 country 
 */
public class Country {

	protected String name;
	protected String twoLetter;
	protected String threeLetter;
	protected String number;

	protected Country(String name, String twoLetter, String threeLetter, String number) {
		this.name = name;
		this.twoLetter = twoLetter;
		this.threeLetter = threeLetter;
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public String getTwoLetter() {
		return twoLetter;
	}

	public String getThreeLetter() {
		return threeLetter;
	}

	public String getNumber() {
		return number;
	}

	@Override
	public boolean equals(Object obj) {
		return this.twoLetter.equals(((Country) obj).getTwoLetter());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[name=");
		builder.append(name);
		builder.append(", number=");
		builder.append(number);
		builder.append(", threeLetter=");
		builder.append(threeLetter);
		builder.append(", twoLetter=");
		builder.append(twoLetter);
		return builder.toString();
	}
	
	

}
