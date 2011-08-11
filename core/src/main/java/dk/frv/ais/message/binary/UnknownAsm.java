package dk.frv.ais.message.binary;

import dk.frv.ais.binary.BinArray;
import dk.frv.ais.binary.SixbitEncoder;
import dk.frv.ais.binary.SixbitException;

public class UnknownAsm extends AisApplicationMessage {
	
	private BinArray binArray;

	public UnknownAsm(int dac, int fi) {
		super(dac, fi);		
	}
	
	@Override
	public void parse(BinArray binArray) throws SixbitException {
		this.binArray = binArray;
	}

	@Override
	public SixbitEncoder getEncoded() {
		SixbitEncoder encoder = new SixbitEncoder();
		encoder.append(binArray);
		return encoder;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(", binary length = ");
		builder.append(binArray.size());
		builder.append("]");
		return builder.toString();
	}

}
