package dk.frv.ais.test.sentence;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dk.frv.ais.binary.SixbitException;
import dk.frv.ais.message.AisMessage12;
import dk.frv.ais.sentence.Abm;
import dk.frv.ais.sentence.SendSentence;
import dk.frv.ais.sentence.SentenceException;

public class EncodeParseTest {
	
	/**
	 * Test parsing of encoded ABM message
	 * @throws SixbitException 
	 * @throws SentenceException 
	 */
	@Test
	public void testAbmEncodeParse() throws SixbitException, SentenceException {
		AisMessage12 msg12 = new AisMessage12();
		msg12.setMessage("LONG MESSAGE LONG MESSAGE LONG MESSAGE LONG MESSAGE LONG MESSAGE LONG MESSAGE");
		Abm abm = new Abm();
		abm.setTalker("AI");
		abm.setDestination(992199007);
		abm.setTextData(msg12);
		abm.setSequence(1);
		
		SendSentence[] sentences = abm.split();
		List<String> strSentences = new ArrayList<String>();
		for (SendSentence sendSentence : sentences) {
			strSentences.add(sendSentence.getEncoded());			
		}
		
		System.out.println("SEND_SENTENCE 1: " + strSentences.get(0));
		System.out.println("SEND_SENTENCE 2: " + strSentences.get(1));
		
		abm = new Abm();
		int res = abm.parse(strSentences.get(0));
		Assert.assertEquals(1, res);
		res = abm.parse(strSentences.get(1));
		Assert.assertEquals(0, res);
		
		Assert.assertEquals(1, abm.getSequence());
		Assert.assertEquals(992199007, abm.getDestination());
		Assert.assertEquals(12, abm.getMsgId());
		
		
	}

}
