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
package dk.frv.ais.geo;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * Representation of a WGS84 position and methods for calculating range and bearing
 * between positions.
 */
public class GeoLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private double latitude;
	private double longitude;
	private TimeZone timeZone;
	private static final int DISTANCE = 0;
	private static final int INITIAL_BEARING = 1;
	private static final int FINAL_BEARING = 2;

	/**
	 * Constructor given position and timezone
	 * @param latitude Negative south of equator
	 * @param longitude Negative east of Prime Meridian
	 * @param timeZone
	 */
	public GeoLocation(double latitude, double longitude, TimeZone timeZone) {
		setLatitude(latitude);
		setLongitude(longitude);
		setTimeZone(timeZone);
	}

	/**
	 * Constructor given position and timezone
	 * @param latitude Negative south of equator
	 * @param longitude Negative east of Prime Meridian
	 */
	public GeoLocation(double latitude, double longitude) {
		this();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Copy contructor
	 */
	public GeoLocation(GeoLocation pos) {
		this();
		if (pos != null) {
			this.latitude = pos.latitude;
			this.longitude = pos.longitude;
			if (pos.timeZone != null) {
				this.timeZone = pos.timeZone;
			}
		}
	}

	/**
	 * Location 0,0 GMT
	 */
	public GeoLocation() {
		setLongitude(0);
		setLatitude(0);
		setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	/**
	 * Set latitude
	 * @param latitude [-90,...,90]
	 */
	public void setLatitude(double latitude) {
		if (latitude > 90 || latitude < -90) {
			throw new IllegalArgumentException("Illegal latitude must be between -90 and  90");
		}
		this.latitude = latitude;
	}

	/**
	 * Set latitude with degrees, minutes, seconds and direction
	 * @param degrees [0,...,89[ Use direction for north or south
	 * @param minutes
	 * @param seconds
	 * @param direction N | S
	 */
	public void setLatitude(int degrees, int minutes, double seconds, String direction) {
		double tempLat = degrees + ((minutes + (seconds / 60.0)) / 60.0);
		if (tempLat > 90 || tempLat < 0) {
			throw new IllegalArgumentException("Latitude must be between 0 and  90");
		}
		if (direction.equals("S")) {
			tempLat *= -1;
		} else if (!direction.equals("N")) {
			throw new IllegalArgumentException("Latitude direction must be N or S");
		}
		this.latitude = tempLat;
	}

	/**
	 * Get latitude
	 * @return latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Set longitude
	 * @param longitude [-180,...,180]
	 */
	public void setLongitude(double longitude) {
		if (longitude > 180 || longitude < -180) {
			throw new IllegalArgumentException("Longitude must be between -180 and  180");
		}
		this.longitude = longitude;
	}

	/**
	 * Set longitude with degrees, minutes, seconds and direction
	 * @param degrees [0,...,179[ Use direction for east or west
	 * @param minutes
	 * @param seconds
	 * @param direction E | W
	 */
	public void setLongitude(int degrees, int minutes, double seconds, String direction) {
		double longTemp = degrees + ((minutes + (seconds / 60.0)) / 60.0);
		if (longTemp > 180 || longitude < 0) {
			throw new IllegalArgumentException("Longitude must be between 0 and  180");
		}
		if (direction.equals("W")) {
			longTemp *= -1;
		} else if (!direction.equals("E")) {
			throw new IllegalArgumentException("Longitude direction must be E or W");
		}
		this.longitude = longTemp;
	}

	/**
	 * Get longitude
	 * @return longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Get timezone
	 * @return timezone
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * Set timezone 
	 * @param timezone
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * Calculate initial bearing for great circle route to location
	 * using Thaddeus Vincenty's</a> inverse formula.
	 * 
	 * @param the second location
	 * @return bearing in degrees
	 */
	public double getGeodesicInitialBearing(GeoLocation location) {
		return vincentyFormula(location, INITIAL_BEARING);
	}

	/**
	 * Calculate final bearing for great circle route to location
	 * using Thaddeus Vincenty's</a> inverse formula.
	 * 
	 * @param the second location
	 * @return bearing in degrees
	 */
	public double getGeodesicFinalBearing(GeoLocation location) {
		return vincentyFormula(location, FINAL_BEARING);
	}

	/**
	 * Get great circle distance to location 
	 * @param location
	 * @return distance in meters
	 */
	public double getGeodesicDistance(GeoLocation location) {
		return vincentyFormula(location, DISTANCE);
	}

	/**
	 * Vincenty formula
	 */
	private double vincentyFormula(GeoLocation location, int formula) {
		double a = 6378137;
		double b = 6356752.3142;
		double f = 1 / 298.257223563; // WGS-84 ellipsiod
		double L = Math.toRadians(location.getLongitude() - getLongitude());
		double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(getLatitude())));
		double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(location.getLatitude())));
		double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
		double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

		double lambda = L;
		double lambdaP = 2 * Math.PI;
		double iterLimit = 20;
		double sinLambda = 0;
		double cosLambda = 0;
		double sinSigma = 0;
		double cosSigma = 0;
		double sigma = 0;
		double sinAlpha = 0;
		double cosSqAlpha = 0;
		double cos2SigmaM = 0;
		double C;
		while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0) {
			sinLambda = Math.sin(lambda);
			cosLambda = Math.cos(lambda);
			sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
					* (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
			if (sinSigma == 0) {
				return 0; // co-incident points
			}
			cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
			sigma = Math.atan2(sinSigma, cosSigma);
			sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
			cosSqAlpha = 1 - sinAlpha * sinAlpha;
			cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
			if (Double.isNaN(cos2SigmaM)) {
				cos2SigmaM = 0; // equatorial line
			}
			C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
			lambdaP = lambda;
			lambda = L + (1 - C) * f * sinAlpha
					* (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
		}
		if (iterLimit == 0) {
			return Double.NaN; // formula failed to converge
		}

		double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
		double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
		double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
		double deltaSigma = B
				* sinSigma
				* (cos2SigmaM + B
						/ 4
						* (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma)
								* (-3 + 4 * cos2SigmaM * cos2SigmaM)));
		double distance = b * A * (sigma - deltaSigma);

		// initial bearing
		double fwdAz = Math.toDegrees(Math.atan2(cosU2 * sinLambda, cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
		// final bearing
		double revAz = Math.toDegrees(Math.atan2(cosU1 * sinLambda, -sinU1 * cosU2 + cosU1 * sinU2 * cosLambda));
		if (formula == DISTANCE) {
			return distance;
		} else if (formula == INITIAL_BEARING) {
			return fwdAz;
		} else if (formula == FINAL_BEARING) {
			return revAz;
		} else { // should never happpen
			return Double.NaN;
		}
	}

	/**
	 * Get rhumb line bearing to location 
	 * @param location
	 * @return bearing in degrees
	 */
	public double getRhumbLineBearing(GeoLocation location) {
		double lat1 = Math.toRadians(getLatitude());
		double lat2 = Math.toRadians(location.getLatitude());
		double dLon = Math.toRadians(location.getLongitude() - getLongitude());
		double dPhi = Math.log(Math.tan(lat2 / 2 + Math.PI / 4) / Math.tan(lat1 / 2 + Math.PI / 4));
		if (Math.abs(dLon) > Math.PI) {
			dLon = dLon > 0 ? -(2 * Math.PI - dLon) : (2 * Math.PI + dLon);
		}
		double brng = Math.atan2(dLon, dPhi);
		return (Math.toDegrees(brng) + 360) % 360;
	}

	/**
	 * Get rhumb line distance to location 
	 * @param location
	 * @return distance in meters
	 */
	public double getRhumbLineDistance(GeoLocation location) {
		double R = 6371; // earth's mean radius in km
		double lat1 = Math.toRadians(getLatitude());
		double lat2 = Math.toRadians(location.getLatitude());
		double dLat = Math.toRadians(location.getLatitude() - getLatitude());
		double dLon = Math.toRadians(Math.abs(location.getLongitude() - getLongitude()));
		double dPhi = Math.log(Math.tan(lat2 / 2 + Math.PI / 4) / Math.tan(lat1 / 2 + Math.PI / 4));
		double q;
		if (dPhi == 0) {
			q = Math.cos(lat1);
		} else {
			q = dLat / dPhi;
		}
		// if dLon over 180° take shorter rhumb across 180° meridian:
		if (dLon > Math.PI)
			dLon = 2 * Math.PI - dLon;
		double dist = Math.sqrt(dLat * dLat + q * q * dLon * dLon) * R * 1000;
		return dist;
	}

	/**
	 * Equals method 
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof GeoLocation))
			return false;
		GeoLocation geo = (GeoLocation) object;
		return Double.doubleToLongBits(latitude) == Double.doubleToLongBits(geo.latitude)
				&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(geo.longitude)
				&& (timeZone == null ? geo.timeZone == null : timeZone.equals(geo.timeZone));
	}

	/**
	 * Hash code for the location
	 */
	@Override
	public int hashCode() {
		int result = 17;
		long latLong = Double.doubleToLongBits(latitude);
		long lonLong = Double.doubleToLongBits(longitude);
		int latInt = (int) (latLong ^ (latLong >>> 32));
		int lonInt = (int) (lonLong ^ (lonLong >>> 32));
		result = 37 * result + getClass().hashCode();
		result += 37 * result + latInt;
		result += 37 * result + lonInt;
		result += 37 * result + (timeZone == null ? 0 : timeZone.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GeoLocation [latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", timeZone=");
		builder.append(timeZone);
		builder.append("]");
		return builder.toString();
	}

}