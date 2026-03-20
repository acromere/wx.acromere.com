package com.acromere.wx;

import org.assertj.core.data.Offset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherStationTest {

	@ParameterizedTest
	@MethodSource( "heatIndexValues" )
	void testCalcHeatIndex( double temperature, double humidity, double heatIndex ) {
		assertThat( WeatherStation.calculateHeatIndexImperial( temperature, humidity ) ).isCloseTo( heatIndex, Offset.offset( 0.5 ) );
	}

	// These values come from the NOAA table of values at: https://en.wikipedia.org/wiki/Heat_index
	private static Stream<Arguments> heatIndexValues() {
		return Stream.of(
			Arguments.of( 80, 40, 80 ),
			Arguments.of( 80, 50, 81 ),
			Arguments.of( 80, 60, 82 ),
			Arguments.of( 80, 70, 83 ),
			Arguments.of( 80, 80, 84 ),
			Arguments.of( 80, 90, 86 ),
			Arguments.of( 80, 100, 87 ),
			Arguments.of( 90, 40, 91 ),
			Arguments.of( 90, 50, 95 ),
			Arguments.of( 90, 60, 100 ),
			Arguments.of( 90, 70, 106 ),
			Arguments.of( 90, 80, 113 ),
			Arguments.of( 90, 90, 122 ),
			Arguments.of( 90, 100, 132 ),
			Arguments.of( 100, 40, 109 ),
			Arguments.of( 100, 50, 118 ),
			Arguments.of( 100, 60, 129 ),
			Arguments.of( 110, 40, 136 )
			);
	}

}
