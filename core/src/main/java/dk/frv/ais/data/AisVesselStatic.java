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

import dk.frv.ais.message.ShipTypeCargo;

/** 
 * Class to represent vessel static information 
 */
public abstract class AisVesselStatic extends AisVesselReport {
	
	protected String name;
	protected String callsign;
	protected Byte shipType;
	protected Byte cargo;
	protected ShipTypeCargo shipTypeCargo;
	protected AisTargetDimensions dimensions;
	
	public AisVesselStatic() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public Byte getShipType() {
		return shipType;
	}

	public void setShipType(Byte shipType) {
		this.shipType = shipType;
	}

	public Byte getCargo() {
		return cargo;
	}

	public void setCargo(Byte cargo) {
		this.cargo = cargo;
	}

	public ShipTypeCargo getShipTypeCargo() {
		return shipTypeCargo;
	}

	public void setShipTypeCargo(ShipTypeCargo shipTypeCargo) {
		this.shipTypeCargo = shipTypeCargo;
	}
	
	public AisTargetDimensions getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(AisTargetDimensions dimensions) {
		this.dimensions = dimensions;
	}
	
}
