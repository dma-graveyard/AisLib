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
import dk.frv.ais.message.AisMessage;
import dk.frv.ais.message.AisMessage18;
import dk.frv.ais.message.AisMessage21;
import dk.frv.ais.message.AisMessage24;
import dk.frv.ais.message.AisMessage4;
import dk.frv.ais.message.AisMessage5;
import dk.frv.ais.message.AisPositionMessage;
import dk.frv.ais.proprietary.DmaSourceTag;
import dk.frv.ais.proprietary.GatehouseSourceTag;
import dk.frv.ais.proprietary.IProprietaryTag;

/**
 * Abstract class representing any AIS target
 */
public abstract class AisTarget {
	
	private static int anonymousCounter = 0;
	
	protected int anonymousId;
	protected int mmsi;
	protected MidCountry country;
	protected Date lastReport;
	protected Date created;
	protected AisTargetSourceData sourceData;
	
	public AisTarget() {
		this.created = new Date();
		this.anonymousId = anonymousCounter++;
	}
	
	/**
	 * Update target given AIS message
	 * @param aisMessage
	 */
	public void update(AisMessage aisMessage) {
		// Set last report time
		this.lastReport = new Date();
		// Set MMSI
		this.mmsi = (int)aisMessage.getUserId();
		// Set source data
		if (sourceData == null) {
			sourceData = new AisTargetSourceData();
		}
		if (aisMessage.getTags() != null) {
			for (IProprietaryTag tag : aisMessage.getTags()) {
				if (tag instanceof DmaSourceTag) {
					DmaSourceTag dmaSourceTag = (DmaSourceTag)tag;
					sourceData.setSourceSystem(dmaSourceTag.getSourceName());
				} else if (tag instanceof GatehouseSourceTag) {
					GatehouseSourceTag ghTag = (GatehouseSourceTag)tag;
					String sourceRegion = ghTag.getRegion();
					if (sourceRegion != null) {
						if (sourceRegion.length() == 0) {
							sourceRegion = null;
						} else {
							// TODO This mapping should come from a tag in the future
							if (sourceRegion.equals("802") || sourceRegion.equals("804") || sourceRegion.equals("808")) {
								sourceData.setSourceType("SAT");
							}
						}
					}
					sourceData.setSourceRegion(sourceRegion);
					sourceData.setSourceCountry(ghTag.getCountry());
					if (ghTag.getBaseMmsi() != null) {
						sourceData.setSourceBs(ghTag.getBaseMmsi());
					}
					sourceData.setLastReport(this.lastReport);
				}
			}
		}
		// Set country
		country = MidCountry.getCountryForMmsi(aisMessage.getUserId());
	}
	
	public int getAnonymousId() {
		return anonymousId;
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
	
	/**
	 * Create new AIS target instance based on AIS message
	 * @param aisMessage
	 * @return
	 */
	public static AisTarget createTarget(AisMessage aisMessage) {
		AisTarget target = null;
		if (aisMessage instanceof AisPositionMessage || aisMessage instanceof AisMessage5) {
			target = new AisClassATarget();				
		}
		else if (aisMessage instanceof AisMessage18 || aisMessage instanceof AisMessage24) {
			target = new AisClassBTarget();
		}
		else if (aisMessage instanceof AisMessage4) {
			target = new AisBsTarget(); 
		}
		else if (aisMessage instanceof AisMessage21) {
			target = new AisAtonTarget();
		}
		return target;
	}
	
}
