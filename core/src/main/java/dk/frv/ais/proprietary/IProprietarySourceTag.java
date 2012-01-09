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

import java.util.Date;

import dk.frv.ais.country.MidCountry;

/**
 * Interface for proprietary source tags
 */
public interface IProprietarySourceTag {

	/**
	 * Time of message receival at source
	 * 
	 * @return
	 */
	Date getTimestamp();

	/**
	 * Country origin of message
	 * 
	 * @return
	 */
	MidCountry getCountry();

	/**
	 * Unique region identifier
	 * 
	 * @return
	 */
	String getRegion();

	/**
	 * Base station MMSI
	 * 
	 * @return
	 */
	Long getBaseMmsi();

	/**
	 * Get the original sentence
	 * 
	 * @return
	 */
	String getSentence();

}
