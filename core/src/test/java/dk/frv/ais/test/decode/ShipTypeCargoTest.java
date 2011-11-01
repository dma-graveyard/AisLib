package dk.frv.ais.test.decode;

import org.junit.Assert;
import org.junit.Test;

import dk.frv.ais.message.ShipTypeCargo;

public class ShipTypeCargoTest {
	
	/**
	 * Example of using the custom ShipTypeCargo class
	 * 
	 */
	@Test
	public void parseInts() {
		int pilot = 50;
		int military = 35;
		int medical = 58;
		int wig = 20;
		int cargoD = 74;
		int tankerA = 81; 
				
		// Prepare Ships
		ShipTypeCargo shipPilot = new ShipTypeCargo(pilot);
		ShipTypeCargo shipMilitary = new ShipTypeCargo(military);
		ShipTypeCargo shipMedical = new ShipTypeCargo(medical);
		ShipTypeCargo shipWig = new ShipTypeCargo(wig);
		ShipTypeCargo shipCargoD = new ShipTypeCargo(cargoD);
		ShipTypeCargo shipTankerA = new ShipTypeCargo(tankerA);
		
		System.out.println(shipPilot);
		Assert.assertEquals(shipPilot.prettyType(), "Pilot");
		
		System.out.println(shipMilitary);
		Assert.assertEquals(shipMilitary.prettyType(), "Military");
		
		System.out.println(shipMedical);
		Assert.assertEquals(shipMedical.prettyType(), "Medical");
		
		System.out.println(shipWig);
		Assert.assertEquals(shipWig.prettyType(), "WIG");
		
		System.out.println(shipCargoD);
		Assert.assertEquals(shipCargoD.prettyType(), "Cargo");
		
		System.out.println(shipTankerA);
		Assert.assertEquals(shipTankerA.prettyType(), "Tanker");
		
	}
	
}
