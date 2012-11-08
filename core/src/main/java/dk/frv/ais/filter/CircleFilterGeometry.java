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
package dk.frv.ais.filter;

import dk.frv.ais.geo.GeoLocation;

/**
 * Circle filter
 */
public class CircleFilterGeometry extends FilterGeometry {
	
	// Center of circle
	private GeoLocation center;
	// Meters
	private long radius;
	
	public CircleFilterGeometry(GeoLocation center, long radius) {
		this.center = center;
		this.radius = radius;
	}
	
	@Override
	public boolean isWithin(GeoLocation loc) {
		return (center.getGeodesicDistance(loc) <= radius);
	}
	
}