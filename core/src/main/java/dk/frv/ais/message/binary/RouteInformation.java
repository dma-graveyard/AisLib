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
package dk.frv.ais.message.binary;

import java.util.ArrayList;
import java.util.List;

import dk.frv.ais.binary.BinArray;
import dk.frv.ais.binary.SixbitEncoder;
import dk.frv.ais.binary.SixbitException;
import dk.frv.ais.message.AisPosition;

public abstract class RouteInformation extends AisApplicationMessage {
	
	public enum RouteType {
		NOT_AVAIABLE(0),
		MANDATORY(1),
		RECOMMENDED(2),
		ALTERNATIVE(3),
		RECOMMENDED_THROUGH_ICE(4),
		SHIP_ROUTE(5),
		CANCELLATION(31);
		
		private int type;
		private RouteType(int type) {
	        this.type = type;
	    }
	    public int getType() {
	        return type;
	    }
	}
	
	private int msgLinkId; // 10 bits: Source specific running number linking birary messages
	private int senderClassification; // 3 bits: 0=ship, 1=authority
	private int routeType; // 5 bits
	private int startMonth; // 4 bits
	private int startDay; // 5 bits
	private int startHour; // 5 bits
	private int startMin; // 6 bits
	private int duration; // 18 bits: Minutes until end of validity 0=cancel route	
	private int waypointCount; // 5 bits: 0 - 16
	private List<AisPosition> waypoints; // 55 bits each longitude 28 bit, latitude 27 bit 
	
	public RouteInformation(int fi) {
		super(1, fi);
		this.waypoints = new ArrayList<AisPosition>();
	}
	
	public RouteInformation(int fi, BinArray binArray) throws SixbitException {		
		super(1, fi, binArray);
	}

	@Override
	public SixbitEncoder getEncoded() {
		SixbitEncoder encoder = new SixbitEncoder();
		encoder.addVal(msgLinkId, 10);
		encoder.addVal(senderClassification, 3);
		encoder.addVal(routeType, 5);
		encoder.addVal(startMonth, 4);
		encoder.addVal(startDay, 5);
		encoder.addVal(startHour, 5);
		encoder.addVal(startMin, 6);
		encoder.addVal(duration, 18);
		encoder.addVal(waypointCount, 5);
		for (AisPosition waypoint : waypoints) {
			encoder.addVal(waypoint.getRawLongitude(), 28);
			encoder.addVal(waypoint.getRawLatitude(), 27);
		}
		return encoder;
	}

	@Override
	public void parse(BinArray binArray) throws SixbitException {
		this.waypoints = new ArrayList<AisPosition>();
		this.msgLinkId = (int)binArray.getVal(10);
		this.senderClassification = (int)binArray.getVal(3);
		this.routeType = (int)binArray.getVal(5);
		this.startMonth = (int)binArray.getVal(4);
		this.startDay = (int)binArray.getVal(5);
		this.startHour = (int)binArray.getVal(5);
		this.startMin = (int)binArray.getVal(6);
		this.duration = (int)binArray.getVal(18);
		this.waypointCount = (int)binArray.getVal(5);
		for (int i=0; i < this.waypointCount; i++) {
			AisPosition waypoint = new AisPosition();
			waypoint.setRawLongitude(binArray.getVal(28));
			waypoint.setRawLatitude(binArray.getVal(27));
			this.waypoints.add(waypoint);
		}
	}

	public int getMsgLinkId() {
		return msgLinkId;
	}

	public void setMsgLinkId(int msgLinkId) {
		this.msgLinkId = msgLinkId;
	}

	public int getSenderClassification() {
		return senderClassification;
	}

	public void setSenderClassification(int senderClassification) {
		this.senderClassification = senderClassification;
	}

	public int getRouteType() {
		return routeType;
	}

	public void setRouteType(int routeType) {
		this.routeType = routeType;
	}

	public int getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}

	public int getStartDay() {
		return startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMin() {
		return startMin;
	}

	public void setStartMin(int startMin) {
		this.startMin = startMin;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getWaypointCount() {
		return waypointCount;
	}

	public void setWaypointCount(int waypointCount) {
		this.waypointCount = waypointCount;
	}

	public List<AisPosition> getWaypoints() {
		return waypoints;
	}
	
	public void setWaypoints(List<AisPosition> waypoints) {
		this.waypoints = waypoints;
	}

	public void addWaypoint(AisPosition waypoint) {
		waypoints.add(waypoint);
		waypointCount = waypoints.size();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(", duration=");
		builder.append(duration);
		builder.append(", msgLinkId=");
		builder.append(msgLinkId);
		builder.append(", routeType=");
		builder.append(routeType);
		builder.append(", senderClassification=");
		builder.append(senderClassification);
		builder.append(", startDay=");
		builder.append(startDay);
		builder.append(", startHour=");
		builder.append(startHour);
		builder.append(", startMin=");
		builder.append(startMin);
		builder.append(", startMonth=");
		builder.append(startMonth);
		builder.append(", waypointCount=");
		builder.append(waypointCount);
		builder.append(", waypoints=");
		builder.append(waypoints);
		return builder.toString();
	}
	
}
