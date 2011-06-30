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
package dk.frv.ais.examples.dumper;

import java.util.Date;

import dk.frv.ais.binary.SixbitException;
import dk.frv.ais.handler.IAisHandler;
import dk.frv.ais.message.AisBinaryMessage;
import dk.frv.ais.message.AisMessage;
import dk.frv.ais.message.binary.AisApplicationMessage;

public class MessageHandler implements IAisHandler {

	@Override
	public void receive(AisMessage aisMessage) {
		
		System.out.println("---");
				
		// Maybe dump source tag, and mark old data
		if (aisMessage.getSourceTag() != null) {
			Date now = new Date();
			long diff = (now.getTime() - aisMessage.getSourceTag().getTimestamp().getTime()) / 1000;
			if (Math.abs(diff) > 5) {
				System.out.println("OLD DATA diff: " + diff);
			}
			System.out.println(aisMessage.getSourceTag());
		}
		// Print original line
		System.out.println(aisMessage.getVdm().getOrgLinesJoined());

		
		// Just dump the message
		System.out.println(aisMessage.toString());		
		
		// Check for binary message
		if (aisMessage.getMsgId() == 6 || aisMessage.getMsgId() == 8) {
			AisBinaryMessage binaryMessage = (AisBinaryMessage)aisMessage;
			try {
				AisApplicationMessage appMessage = binaryMessage.getApplicationMessage();
				System.out.println(appMessage);
			} catch (SixbitException e) {
			}
			
		}
	}

}
