package com.acromere.wx;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CardinalTest {

	@Test
	public void testToCardinalPrimary() {
		assertThat( Cardinal.toCardinal( 0 ) ).isEqualTo( Cardinal.N );
		assertThat( Cardinal.toCardinal( 90 ) ).isEqualTo( Cardinal.E );
		assertThat( Cardinal.toCardinal( 180 ) ).isEqualTo( Cardinal.S );
		assertThat( Cardinal.toCardinal( 270 ) ).isEqualTo( Cardinal.W );
	}

	@Test
	public void testToCardinalSecondary() {
		assertThat( Cardinal.toCardinal( 45 ) ).isEqualTo( Cardinal.NE );
		assertThat( Cardinal.toCardinal( 135 ) ).isEqualTo( Cardinal.SE );
		assertThat( Cardinal.toCardinal( 225 ) ).isEqualTo( Cardinal.SW );
		assertThat( Cardinal.toCardinal( 315 ) ).isEqualTo( Cardinal.NW );
	}

	@Test
	public void testToCardinalTerciary() {
		double origin = 45.0 / 2;
		assertThat( Cardinal.toCardinal( origin ) ).isEqualTo( Cardinal.NNE );
		assertThat( Cardinal.toCardinal( origin + 45 ) ).isEqualTo( Cardinal.ENE );
		assertThat( Cardinal.toCardinal( origin + 2 * 45 ) ).isEqualTo( Cardinal.ESE );
		assertThat( Cardinal.toCardinal( origin + 3 * 45 ) ).isEqualTo( Cardinal.SSE );

		assertThat( Cardinal.toCardinal( origin + 4 * 45 ) ).isEqualTo( Cardinal.SSW );
		assertThat( Cardinal.toCardinal( origin + 5 * 45 ) ).isEqualTo( Cardinal.WSW );
		assertThat( Cardinal.toCardinal( origin + 6 * 45 ) ).isEqualTo( Cardinal.WNW );
		assertThat( Cardinal.toCardinal( origin + 7 * 45 ) ).isEqualTo( Cardinal.NNW );
	}

}
