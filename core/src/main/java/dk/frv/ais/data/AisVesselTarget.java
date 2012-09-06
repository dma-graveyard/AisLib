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

/** 
 * Class to represent any vessel target 
 */
public abstract class AisVesselTarget extends AisTarget {
	
	protected AisVesselStatic vesselStatic;
	protected AisVesselPosition vesselPosition;
	
	public AisVesselTarget() {
		super();
	}

	public AisVesselStatic getVesselStatic() {
		return vesselStatic;
	}

	public void setVesselStatic(AisVesselStatic vesselStatic) {
		this.vesselStatic = vesselStatic;
	}

	public AisVesselPosition getVesselPosition() {
		return vesselPosition;
	}

	public void setVesselPosition(AisVesselPosition vesselPosition) {
		this.vesselPosition = vesselPosition;
	}
	
}
