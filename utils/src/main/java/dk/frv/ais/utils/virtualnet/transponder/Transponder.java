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
package dk.frv.ais.utils.virtualnet.transponder;

import dk.frv.ais.handler.IAisHandler;
import dk.frv.ais.message.AisMessage;

/**
 * Class that represents a virtual AIS transponder
 */
public class Transponder implements IAisHandler {
	
	private long mmsi;
	private int tcpPort;
	
	// Reader thread
	
	// Writer thread
	
	public Transponder() {
		
	}

	@Override
	public void receive(AisMessage aisMessage) {
		// If source.mmsi == mmsi then set own
		
		// Get original lines but do conversion to own if own
		aisMessage.getVdm().getOrgLines();
		
		// Send to writer thread
		
	}

	
}
