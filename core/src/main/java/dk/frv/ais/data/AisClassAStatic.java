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

/**
 * Class to represent class A statics
 */
public class AisClassAStatic extends AisVesselStatic {
	
	private Integer imoNo;
	private String destination;
	private Date eta;
	private byte posType;
	private Short draught;
	private byte version;
	private byte dte;
	
	public AisClassAStatic() {
		super();
	}

	public Integer getImoNo() {
		return imoNo;
	}

	public void setImoNo(Integer imoNo) {
		this.imoNo = imoNo;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public byte getPosType() {
		return posType;
	}

	public void setPosType(byte posType) {
		this.posType = posType;
	}

	public Short getDraught() {
		return draught;
	}

	public void setDraught(Short draught) {
		this.draught = draught;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getDte() {
		return dte;
	}

	public void setDte(byte dte) {
		this.dte = dte;
	}
	
}
