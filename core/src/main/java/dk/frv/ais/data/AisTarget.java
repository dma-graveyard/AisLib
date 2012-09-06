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
package dk.frv.ais.data;

import java.util.Date;

import dk.frv.ais.country.MidCountry;

/**
 * Abstract class representing any AIS target
 */
public abstract class AisTarget {
	
	protected int mmsi;
	protected MidCountry country;
	protected Date lastReport;
	protected Date created;
	protected AisTargetSourceData sourceData;
	
	public AisTarget() {
		this.created = new Date();
	}

	public int getMmsi() {
		return mmsi;
	}

	public void setMmsi(int mmsi) {
		this.mmsi = mmsi;
	}

	public MidCountry getCountry() {
		return country;
	}

	public void setCountry(MidCountry country) {
		this.country = country;
	}

	public Date getLastReport() {
		return lastReport;
	}

	public void setLastReport(Date lastReport) {
		this.lastReport = lastReport;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public AisTargetSourceData getSourceData() {
		return sourceData;
	}
	
	public void setSourceData(AisTargetSourceData sourceData) {
		this.sourceData = sourceData;
	}
	
}
