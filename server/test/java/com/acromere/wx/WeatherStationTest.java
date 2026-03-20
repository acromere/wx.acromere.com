package com.acromere.wx;

import org.assertj.core.data.Offset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherStationTest {

	@ParameterizedTest
	@MethodSource( "windChillValues" )
	void testCalcWindChill( double temperature, double windSpeed, double windChill ) {
		assertThat( WeatherStation.calculateWindChill( temperature, windSpeed ) ).isCloseTo( windChill, Offset.offset( 0.05 ) );
	}

	private static Stream<Arguments> windChillValues() {
		return Stream.of(
			Arguments.of( -10, 20, -17.8 ), Arguments.of( -10, 5, -10 ), // Threshold: <= 5 wind
			Arguments.of( 5, 20, 5 ), // Threshold: >= 5 temp
			Arguments.of( 0, 10, -3.3 ), Arguments.of( -5, 10, -9.3 ), Arguments.of( -15, 30, -26.0 ), Arguments.of( -20, 40, -34.1 ), Arguments.of( -25, 50, -42.2 )
		);
	}

	@ParameterizedTest
	@MethodSource( "heatIndexValues" )
	void testCalcHeatIndex( double temperature, double humidity, double heatIndex ) {
		assertThat( WeatherStation.calculateHeatIndex( temperature, humidity ) ).isCloseTo( heatIndex, Offset.offset( 0.5 ) );
	}

	private static Stream<Arguments> heatIndexValues() {
		return Stream.of(
			Arguments.of( 25, 50, 25 ), // Threshold: < 27 temp
			Arguments.of( 30, 30, 30 ), // Threshold: < 40 humidity
			Arguments.of( 26.67, 40, 26.67 ), // 80 F, 40% (Threshold: 80F/26.67C)
			Arguments.of( 27.22, 50, 27.78 ), // 81 F, 50% -> 82.25 F -> 27.92 C (close to 27.78/82F)
			Arguments.of( 27.22, 70, 29.44 ), // 81 F, 70% -> 85.12 F -> 29.51 C (close to 29.44/85F)
			Arguments.of( 32.22, 40, 32.78 ), // 90 F, 40% -> 91 F -> 32.78 C
			Arguments.of( 32.22, 60, 37.78 ), // 90 F, 60% -> 100 F -> 37.78 C
			Arguments.of( 37.78, 40, 42.78 ), // 100 F, 40% -> 109 F -> 42.78 C
			Arguments.of( 37.78, 60, 53.89 )  // 100 F, 60% -> 129 F -> 53.89 C
		);
	}

	@ParameterizedTest
	@MethodSource( "windChillImperialValues" )
	void testCalcWindChillImperial( double temperature, double windSpeed, double windChill ) {
		assertThat( WeatherStation.calculateWindChillImperial( temperature, windSpeed ) ).isCloseTo( windChill, Offset.offset( 0.5 ) );
	}

	private static Stream<Arguments> windChillImperialValues() {
		return Stream.of(
			Arguments.of( 35, 3, 35 ), // Threshold: <= 3 wind
			Arguments.of( 50, 10, 50 ), // Threshold: >= 50 temp
			Arguments.of( 35, 5, 31 ),
			Arguments.of( 35, 10, 27 ),
			Arguments.of( 35, 15, 25 ),
			Arguments.of( 35, 20, 24 ),
			Arguments.of( 35, 25, 23 ),
			Arguments.of( 10, 5, 1 ),
			Arguments.of( 10, 10, -4 ),
			Arguments.of( 10, 15, -7 ),
			Arguments.of( 10, 20, -9 ),
			Arguments.of( 10, 25, -11 ),
			Arguments.of( -15, 5, -28 ),
			Arguments.of( -15, 10, -35 ),
			Arguments.of( -15, 15, -39 ),
			Arguments.of( -15, 20, -42 ),
			Arguments.of( -15, 25, -44 )
		);
	}

	@ParameterizedTest
	@MethodSource( "heatIndexImperialValues" )
	void testCalcHeatIndexImperial( double temperature, double humidity, double heatIndex ) {
		assertThat( WeatherStation.calculateHeatIndexImperial( temperature, humidity ) ).isCloseTo( heatIndex, Offset.offset( 0.5 ) );
	}

	// These values come from the NOAA table of values at: https://en.wikipedia.org/wiki/Heat_index
	private static Stream<Arguments> heatIndexImperialValues() {
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
