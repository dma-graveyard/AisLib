package dk.frv.ais.test.encode;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import dk.frv.ais.binary.SixbitException;
import dk.frv.ais.message.AisMessage12;
import dk.frv.ais.reader.SendException;
import dk.frv.ais.reader.SendRequest;
import dk.frv.ais.sentence.Abm;

public class EncodeSendTest {

	@Test
	public void encodeSrmAbmTest() throws SendException, SixbitException {
		int destination = 992199007;
		String message = "START TEST TEST TEST END";

		AisMessage12 msg12 = new AisMessage12();
		msg12.setDestination(destination);
		msg12.setMessage(message);

		SendRequest sendRequest = new SendRequest(msg12, 1, destination);

		String[] sentences = sendRequest.createSentences();
		System.out.println("MSG12 SendRequest sentences:\n" + StringUtils.join(sentences, "\r\n"));

		Abm abm = new Abm();
		abm.setTextData(msg12);
		abm.setSequence(0);
		abm.setTotal(1);
		abm.setNum(1);
		String encoded = abm.getEncoded();
		System.out.println("MSG12 ABM encoded: " + encoded);
	}

}
