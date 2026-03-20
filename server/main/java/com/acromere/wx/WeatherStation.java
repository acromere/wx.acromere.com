package com.acromere.wx;

import lombok.Getter;
import lombok.Setter;
import org.shredzone.commons.suncalc.SunPosition;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

@Getter
@Setter
public class WeatherStation {

	private static final String DEGREE = "°";

	// Weather station

	private String id;

	private String name;

	private double latitude;

	private double longitude;

	private double elevation;

	private String serverVersion;

	private long timestamp;

	private boolean polled;

	// Basic weather measures
	private double temperature;

	private double pressure;

	private double humidity;

	private double dewPoint;

	private double windChill;

	private double heatIndex;

	private double feelsLike;

	private double windSpeed;

	private double windGust;

	private double windDirection;

	private Cardinal windCardinal;

	private double rainTotalDaily;

	private double rainRate;

	// Avg, min, max and trends
	private double temperatureTrend;

	private double humidityTrend;

	private double pressureTrend;

	private double windSpeedTrend;

	private double windTenMinMax;

	private double windTenMinAvg;

	private double windTenMinMin;

	private double windTwoMinMax;

	private double windTwoMinAvg;

	private double windTwoMinMin;

	private double windDirectionTenMinAvg;

	private Cardinal windCardinalTenMinAvg;

	private double windDirectionTwoMinAvg;

	private Cardinal windCardinalTwoMinAvg;

	private double sunAltitude;

	private double sunIllumination;

	private boolean postMeridian;

	// Unit values
	private String temperatureUnit = DEGREE + "F";

	private String humidityUnit = "%";

	private String pressureUnit = "in";

	private String windSpeedUnit = "mph";

	private String windDirectionUnit = DEGREE;

	private String rainUnit = "in";

	private String rainRateUnit = rainUnit + "/hr";

	private String temperatureTrendUnit = temperatureUnit + "/hr";

	private String humidityTrendUnit = humidityUnit + "/hr";

	private String pressureTrendUnit = pressureUnit + "/hr";

	private String windSpeedTrendUnit = windSpeedUnit + "/hr";

	private String sunAltitudeUnit = DEGREE;

	private String sunIlluminationUnit = "%";

	private final FlightCondition flightCondition;

	@SuppressWarnings( "unused" )
	public WeatherStation() {
		this( null, null, 0, 0 );
	}

	public WeatherStation( String id ) {
		this( id, null, 0, 0 );
	}

	public WeatherStation( String id, String name, double latitude, double longitude ) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.flightCondition = new FlightCondition();
	}

	public void setWindDirection( double windDirection ) {
		this.windDirection = windDirection;
		this.windCardinal = Cardinal.toCardinal( windDirection );
	}

	public void setWindDirectionTenMinAvg( double windDirectionTenMinAvg ) {
		this.windDirectionTenMinAvg = windDirectionTenMinAvg;
		this.windCardinalTenMinAvg = Cardinal.toCardinal( windDirectionTenMinAvg );
	}

	public void setWindDirectionTwoMinAvg( double windDirectionTwoMinAvg ) {
		this.windDirectionTwoMinAvg = windDirectionTwoMinAvg;
		this.windCardinalTwoMinAvg = Cardinal.toCardinal( windDirectionTwoMinAvg );
	}

	public void copyFrom( WeatherStation that ) {
		this.setTimestamp( that.getTimestamp() );

		this.setTemperature( that.getTemperature() );
		this.setPressure( that.getPressure() );
		this.setHumidity( that.getHumidity() );
		this.setDewPoint( that.getDewPoint() );
		this.setWindChill( that.getWindChill() );
		this.setHeatIndex( that.getHeatIndex() );
		this.setFeelsLike( calcFeelsLike( that.getTemperature(), that.getWindTenMinAvg(), that.getHumidity() ) );
		this.setWindSpeed( that.getWindSpeed() );
		this.setWindDirection( that.getWindDirection() );
		this.setRainTotalDaily( that.getRainTotalDaily() );
		this.setRainRate( that.getRainRate() );

		this.setTemperatureTrend( that.getTemperatureTrend() );
		this.setHumidityTrend( that.getHumidityTrend() );
		this.setPressureTrend( that.getPressureTrend() );
		this.setWindSpeedTrend( that.getWindSpeedTrend() );

		this.setWindTenMinMax( that.getWindTenMinMax() );
		this.setWindTenMinAvg( that.getWindTenMinAvg() );
		this.setWindTenMinMin( that.getWindTenMinMin() );
		this.setWindTwoMinMax( that.getWindTwoMinMax() );
		this.setWindTwoMinAvg( that.getWindTwoMinAvg() );
		this.setWindTwoMinMin( that.getWindTwoMinMin() );
		this.setWindDirectionTenMinAvg( that.getWindDirectionTenMinAvg() );
		this.setWindDirectionTwoMinAvg( that.getWindDirectionTwoMinAvg() );

		this.setTemperatureUnit( that.getTemperatureUnit() );
		this.setHumidityUnit( that.getHumidityUnit() );
		this.setPressureUnit( that.getPressureUnit() );
		this.setWindSpeedUnit( that.getWindSpeedUnit() );
		this.setWindDirectionUnit( that.getWindDirectionUnit() );
		this.setRainUnit( that.getRainUnit() );
		this.setRainRateUnit( that.getRainRateUnit() );
		this.setTemperatureTrendUnit( that.getTemperatureTrendUnit() );
		this.setHumidityTrendUnit( that.getHumidityTrendUnit() );
		this.setPressureTrendUnit( that.getPressureTrendUnit() );
		this.setWindSpeedTrendUnit( that.getWindSpeedTrendUnit() );
	}

	public void updateExtendedValues() {
		this.setFeelsLike( calcFeelsLike( getTemperature(), getWindSpeed(), getHumidity() ) );

		// Using the sun altitude, calculate an illumination value
		// Civil twilight is -6 degrees (https://en.wikipedia.org/wiki/Twilight)
		SunPosition position = SunPosition.compute().on( new Date() ).at( latitude, longitude ).execute();
		double sunAltitude = position.getTrueAltitude();
		this.setSunAltitude( sunAltitude );
		this.setSunIllumination( sunAltitude <= 0 ? 0 : Math.sin( Math.toRadians( sunAltitude ) ) * 100 );
		this.setPostMeridian( position.getAzimuth() > 180.0 );
	}

	private double calcFeelsLike( double temperature, double wind, double humidity ) {
		if( temperature < 50 ) return calculateWindChill( temperature, wind );
		if( temperature > 80 ) return calculateHeatIndex( temperature, humidity );
		return temperature;
	}

	public static double calculateWindChill( double t, double w ) {
		if( w <= 3 || t >= 50 ) return t;

		return 35.74f + 0.6215f * t - 35.75f * Math.pow( w, 0.16 ) + 0.4275f * t * Math.pow( w, 0.16 );
	}

	public static double calculateHeatIndex( double t, double h ) {
		if( t < 80 || h < 40 ) return t;

		double c1 = -42.379;
		double c2 = 2.04901523;
		double c3 = 10.14333127;
		double c4 = -0.22475541;
		double c5 = -6.83783e-3;
		double c6 = -5.481717e-2;
		double c7 = 1.22874e-3;
		double c8 = 8.5282e-4;
		double c9 = -1.99e-6;

		double t2 = t * t;
		double h2 = h * h;

		double heatIndex = c1 + c2 * t + c3 * h + c4 * t * h + c5 * t2 + c6 * h2 + c7 * t2 * h + c8 * t * h2 + c9 * t2 * h2;
		return Math.max( t, heatIndex );
	}

	public void updateFlightConditions() {
		getFlightCondition().reset();

		// Temperature
		double temperature = getTemperature();
		if( temperature >= 35 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.HOT );
		} else if( temperature < 35 && temperature >= 30 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.HOT );
		} else if( temperature < 30 && temperature >= 25 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, FlightCondition.Reason.HOT );
		} else if( temperature < 25 && temperature >= 20 ) {
			updateFlightCondition( FlightCondition.Summary.GOOD, FlightCondition.Reason.WARM );
		} else if( temperature < 20 && temperature >= 15 ) {
			updateFlightCondition( FlightCondition.Summary.GREAT );
		} else if( temperature < 15 && temperature >= 10 ) {
			updateFlightCondition( FlightCondition.Summary.GOOD, FlightCondition.Reason.COOL );
		} else if( temperature < 10 && temperature >= 5 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, FlightCondition.Reason.COLD );
		} else if( temperature < 5 && temperature >= 0 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.COLD );
		} else if( temperature < 0 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.COLD );
		}

		// Wind
		double wind = getWindSpeed();
		double gust = getWindGust();
		if( wind >= 40 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.WINDY );
		} else if( wind >= 30 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.WINDY );
		} else if( wind >= 20 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, FlightCondition.Reason.BREEZY );
		} else if( wind >= 10 ) {
			updateFlightCondition( FlightCondition.Summary.GOOD, FlightCondition.Reason.BREEZY );
		} // otherwise GREAT

		if( gust >= 50 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.GUSTY );
		} else if( gust >= 40 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.GUSTY );
		} else if( gust >= 30 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, FlightCondition.Reason.BUMPY );
		} else if( gust >= 20 ) {
			updateFlightCondition( FlightCondition.Summary.GOOD, FlightCondition.Reason.BUMPY );
		} // otherwise GREAT

		double sun = getSunAltitude();
		boolean isPm = isPostMeridian();
		FlightCondition.Reason twilight = isPm ? FlightCondition.Reason.DUSK : FlightCondition.Reason.DAWN;
		if( sun <= -5 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.DARK );
		} else if( sun <= 0 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, twilight );
		} else if( sun <= 5 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, twilight );
		} // otherwise GREAT

		if( getRainRate() > 0 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.RAINY );
		} // otherwise GREAT
	}

	public void updateFlightConditionsImperial() {
		getFlightCondition().reset();

		double temperature = getTemperature();
		if( temperature >= 100 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.HOT );
		} else if( temperature < 100 && temperature >= 90 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.HOT );
		} else if( temperature < 90 && temperature >= 80 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, FlightCondition.Reason.HOT );
		} else if( temperature < 80 && temperature >= 70 ) {
			updateFlightCondition( FlightCondition.Summary.GOOD, FlightCondition.Reason.WARM );
		} else if( temperature < 70 && temperature >= 60 ) {
			updateFlightCondition( FlightCondition.Summary.GREAT );
		} else if( temperature < 60 && temperature >= 50 ) {
			updateFlightCondition( FlightCondition.Summary.GOOD, FlightCondition.Reason.COOL );
		} else if( temperature < 50 && temperature >= 40 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, FlightCondition.Reason.COLD );
		} else if( temperature < 40 && temperature >= 30 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.COLD );
		} else if( temperature < 30 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.COLD );
		}

		double wind = getWindTenMinAvg();
		double gust = getWindTwoMinMax();

		if( wind >= 20 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.WINDY );
		} else if( wind >= 15 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.WINDY );
		} else if( wind >= 10 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, FlightCondition.Reason.BREEZY );
		} else if( wind >= 5 ) {
			updateFlightCondition( FlightCondition.Summary.GOOD, FlightCondition.Reason.BREEZY );
		} // otherwise GREAT

		if( gust >= 30 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.GUSTY );
		} else if( gust >= 20 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.GUSTY );
		} else if( gust >= 15 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, FlightCondition.Reason.BUMPY );
		} else if( gust >= 10 ) {
			updateFlightCondition( FlightCondition.Summary.GOOD, FlightCondition.Reason.BUMPY );
		} // otherwise GREAT

		double sun = getSunAltitude();
		boolean isPm = isPostMeridian();
		FlightCondition.Reason twilight = isPm ? FlightCondition.Reason.DUSK : FlightCondition.Reason.DAWN;
		if( sun <= -5 ) {
			updateFlightCondition( FlightCondition.Summary.HOLD, FlightCondition.Reason.DARK );
		} else if( sun <= 0 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, twilight );
		} else if( sun <= 5 ) {
			updateFlightCondition( FlightCondition.Summary.FAIR, twilight );
		} // otherwise GREAT

		if( getRainRate() > 0 ) {
			updateFlightCondition( FlightCondition.Summary.POOR, FlightCondition.Reason.RAINY );
		} // otherwise GREAT
	}

	private void updateFlightCondition( FlightCondition.Summary summary ) {
		updateFlightCondition( summary, null );
	}

	private void updateFlightCondition( FlightCondition.Reason reason ) {
		updateFlightCondition( null, reason );
	}

	private void updateFlightCondition( FlightCondition.Summary summary, FlightCondition.Reason reason ) {
		if( summary != null && summary.ordinal() > getFlightCondition().getSummary().ordinal() ) getFlightCondition().setSummary( summary );
		if( reason != null ) getFlightCondition().getReasons().add( reason );
		getFlightCondition().getReasons().sort( null );
	}

	public boolean equals( Object o ) {
		if( o == this ) return true;
		if( !(o instanceof WeatherStation other) ) return false;
		if( !other.canEqual( this ) ) return false;
		return Objects.equals( this.getId(), other.getId() );
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}

	protected boolean canEqual( Object other ) {return other instanceof WeatherStation;}

	public String toString() {
		return "WeatherStation(id=" + this.getId() + ", name=" + this.getName() + ", timestamp=" + this.getTimestamp() + ", temperature=" + this.getTemperature() + ", pressure=" + this.getPressure() + ", humidity=" + this.getHumidity() + ", dewPoint=" + this.getDewPoint() + ", windChill=" + this.getWindChill() + ", heatIndex=" + this.getHeatIndex() + ", wind=" + this.getWindSpeed() + ", windDirection=" + this.getWindDirection() + ", rainTotalDaily=" + this.getRainTotalDaily() + ", rainRate=" + this.getRainRate() + ")";
	}

}
