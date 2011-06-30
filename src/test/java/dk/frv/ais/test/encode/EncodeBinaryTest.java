package dk.frv.ais.test.encode;

import org.junit.Assert;
import org.junit.Test;

import dk.frv.ais.binary.SixbitEncoder;
import dk.frv.ais.binary.SixbitException;
import dk.frv.ais.message.AisMessage6;
import dk.frv.ais.message.binary.Capability;
import dk.frv.ais.sentence.Abm;
import dk.frv.ais.sentence.Vdm;

public class EncodeBinaryTest {
		
	/**
	 * Test encoding a binary AIS message
	 * @throws BitExhaustionException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void binaryEncode() throws IllegalArgumentException, SixbitException {
		Capability capability = new Capability();
		capability.setReqDac(296);
		SixbitEncoder encoder = capability.getEncoded();
		
		int destination = 992199007;

		AisMessage6 msg6 = new AisMessage6();
		msg6.setUserId(219015063);
		msg6.setDestination(destination);
		msg6.setRetransmit(0);
		msg6.setAppMessage(capability);
		encoder = msg6.getEncoded();
		String encoded = encoder.encode();
		String expected = "63@oWUkdShEt04=:000000000000";
		System.out.println("encoded:  " + encoded);
		System.out.println("expected: " + expected);
		System.out.println("pad bits: " + encoder.getPadBits());
		Assert.assertTrue(encoded.equals(expected));
		
		Vdm vdm = new Vdm();
		vdm.setTalker("AI");
		vdm.setTotal(1);
		vdm.setNum(1);
		vdm.setMessageData(msg6);
		vdm.setSequence(0);
		encoded = vdm.getEncoded();
		System.out.println("VDM encoded: " + encoded);	
		
		Abm abm = new Abm();
		abm.setTalker("AI");
		abm.setTotal(1);
		abm.setNum(1);
		abm.setSequence(0);
		abm.setBinaryData(msg6);
		abm.setDestination(destination);
		encoded = abm.getEncoded();
		System.out.println("ABM encoded: " + encoded);
		
	}
	


}
