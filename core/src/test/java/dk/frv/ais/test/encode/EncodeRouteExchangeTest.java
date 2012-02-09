package dk.frv.ais.test.encode;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import dk.frv.ais.binary.SixbitException;
import dk.frv.ais.geo.GeoLocation;
import dk.frv.ais.message.AisMessage;
import dk.frv.ais.message.AisMessage8;
import dk.frv.ais.message.AisMessageException;
import dk.frv.ais.message.AisPosition;
import dk.frv.ais.message.binary.AisApplicationMessage;
import dk.frv.ais.message.binary.BroadcastIntendedRoute;
import dk.frv.ais.sentence.Bbm;
import dk.frv.ais.sentence.SentenceException;
import dk.frv.ais.sentence.Vdm;

public class EncodeRouteExchangeTest {

	@Test
	public void encodeIntendedTest() throws SixbitException, SentenceException, AisMessageException {
		int userId = 992199007;

		// Make ASM
		BroadcastIntendedRoute route = new BroadcastIntendedRoute();
		route.setStartMonth(11);
		route.setStartDay(3);
		route.setStartHour(16);
		route.setStartMin(50);
		route.setDuration(35);
		// Add waypoints
		List<AisPosition> waypoints = new ArrayList<AisPosition>();
		waypoints.add(new AisPosition(new GeoLocation(55.845283333333334, 12.704933333333333)));
		waypoints.add(new AisPosition(new GeoLocation(55.913383333333336, 12.6453)));
		waypoints.add(new AisPosition(new GeoLocation(55.93476666666667, 12.644016666666667)));
		waypoints.add(new AisPosition(new GeoLocation(55.97728333333333, 12.7015)));
		waypoints.add(new AisPosition(new GeoLocation(56.00, 12.8)));
		waypoints.add(new AisPosition(new GeoLocation(56.10, 12.9)));
		for (AisPosition aisPosition : waypoints) {
			route.addWaypoint(aisPosition);
		}

		// Make MSG8
		AisMessage8 msg8 = new AisMessage8();
		msg8.setUserId(userId);
		msg8.setAppMessage(route);

		System.out.println("Route message 8: " + msg8);

		// Make BBM sentences
		Bbm bbm = new Bbm();
		bbm.setTalker("AI");
		bbm.setTotal(1);
		bbm.setNum(1);
		bbm.setBinaryData(msg8);
		bbm.setSequence(0);
		String encoded = bbm.getEncoded();
		System.out.println("Route BBM encoded: " + encoded);

		// Make VDM sentences
		String[] vdms = Vdm.createSentences(msg8, 0);
		System.out.println("Route VDM encoded:\n" + StringUtils.join(vdms, "\r\n"));

		// Decode VDM sentences
		Vdm vdm = new Vdm();
		for (int i = 0; i < vdms.length; i++) {
			int result = vdm.parse(vdms[i]);
			if (i < vdms.length - 1) {
				Assert.assertEquals(result, 1);
			} else {
				Assert.assertEquals(result, 0);
			}

		}
		// Extract AisMessage6
		msg8 = (AisMessage8) AisMessage.getInstance(vdm);
		System.out.println("msg8 decoded: " + msg8);
		// Get the ASM
		AisApplicationMessage appMsg = msg8.getApplicationMessage();
		BroadcastIntendedRoute parsedRoute = (BroadcastIntendedRoute) appMsg;
		System.out.println("msg 8 application: " + appMsg);

		// Assert if mathes original
		Assert.assertEquals(parsedRoute.getWaypointCount(), waypoints.size());
		Assert.assertEquals(route.getDuration(), parsedRoute.getDuration());
		Assert.assertEquals(route.getStartMonth(), parsedRoute.getStartMonth());
		Assert.assertEquals(route.getStartDay(), parsedRoute.getStartDay());
		Assert.assertEquals(route.getStartHour(), parsedRoute.getStartHour());
		Assert.assertEquals(route.getStartMin(), parsedRoute.getStartMin());
		for (int i = 0; i < parsedRoute.getWaypointCount(); i++) {
			AisPosition parsedWp = parsedRoute.getWaypoints().get(i);
			AisPosition orgWp = waypoints.get(i);
			Assert.assertTrue(parsedWp.equals(orgWp));
		}

	}

}
