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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Singleton class that allows to a country by ISO 3166 code or MID
 * (Maritime Identification Digits)
 * 
 * See {@link http://en.wikipedia.org/wiki/Maritime_Mobile_Service_Identity}
 *
 * The mappings are defined in the country.properties file.
 */
public class CountryMapper {

	private static final Logger LOG = Logger.getLogger(CountryMapper.class);

	private static CountryMapper instance = null;

	protected HashMap<String, MidCountry> twoLetterMap = new HashMap<String, MidCountry>();
	protected HashMap<String, MidCountry> threeLetterMap = new HashMap<String, MidCountry>();
	protected HashMap<Integer, MidCountry> midCountryMap = new HashMap<Integer, MidCountry>();

	private CountryMapper() {
		Properties props = new Properties();
		URL url = ClassLoader.getSystemResource("country.properties");
		try {
			props.load(url.openStream());
		} catch (IOException e) {
			LOG.error("Failed to load country.properties: " + e.getMessage());
			return;
		}

		for (Object key : props.keySet()) {
			String a2 = (String) key;
			String val = props.getProperty(a2);
			String[] elems = StringUtils.splitByWholeSeparatorPreserveAllTokens(val, "|");
			MidCountry country = new MidCountry(elems[0], a2, elems[1], elems[2]);
			String[] strMids = StringUtils.split(elems[3], ",");
			for (String strMid : strMids) {
				Integer mid = Integer.parseInt(strMid);
				country.addMid(mid);
				midCountryMap.put(mid, country);
			}
			twoLetterMap.put(country.getTwoLetter(), country);
			threeLetterMap.put(country.getThreeLetter(), country);
		}
	}

	/**
	 * Get MidCountry by ISO 3166 two or three letter code
	 * @param code
	 * @return
	 */
	public MidCountry getByCode(String code) {
		if (code.length() == 2) {
			return twoLetterMap.get(code);
		}
		return threeLetterMap.get(code);
	}

	/**
	 * Get MidCountry by MID
	 * @param mid
	 * @return
	 */
	public MidCountry getByMid(int mid) {
		MidCountry country = midCountryMap.get(mid);
		if (country == null) {
			LOG.warn("Unknown MID " + mid);
		}
		return country;
	}

	public static CountryMapper getInstance() {
		synchronized (CountryMapper.class) {
			if (instance == null) {
				instance = new CountryMapper();
			}
			return instance;
		}
	}

}
