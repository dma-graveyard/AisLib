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
package dk.frv.ais.utils.virtualnet.network;

import java.util.ArrayList;
import java.util.List;

import dk.frv.ais.handler.IAisHandler;
import dk.frv.ais.message.AisMessage;

/**
 * Class that represents the virtual AIS network
 */
public class AisNetwork {
	
	private List<IAisHandler> listeners = new ArrayList<IAisHandler>();
	
	public AisNetwork() {
		
	}
	
	/**
	 * Method to use for broadcasting VDM messages on the virtual network
	 * @param vdms
	 */
	synchronized void broadcast(AisMessage aisMessage) {
		// Send to all listeners
		for (IAisHandler listener : listeners) {
			listener.receive(aisMessage);
		}
	}
	
	/**
	 * Add listener to network
	 * @param listener
	 */
	synchronized void addListener(IAisHandler listener) {
		listeners.add(listener);
	}

}
