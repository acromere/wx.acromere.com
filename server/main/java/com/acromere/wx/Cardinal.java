package com.acromere.wx;

// In meteorology, an azimuth of 000° is used only when no wind is blowing,
// while 360° means the wind is from the North.

public enum Cardinal {

	N,
	NNE,
	NE,
	ENE,
	E,
	ESE,
	SE,
	SSE,
	S,
	SSW,
	SW,
	WSW,
	W,
	WNW,
	NW,
	NNW,
	NON;

	private static final double SLICE = 360.0 / 16.0;

	private static final double OFFSET = SLICE / 2.0;

	public static Cardinal toCardinal( double direction ) {
		double dir = direction % 360.0;
		if( dir < 0 ) dir += 360.0;

		if( dir >= SLICE * 16 - OFFSET || dir <= OFFSET ) {
			return N;
		} else if( dir >= SLICE * 4 - OFFSET && dir <= SLICE * 4 + OFFSET ) {
			return E;
		} else if( dir >= SLICE * 8 - OFFSET && dir <= SLICE * 8 + OFFSET ) {
			return S;
		} else if( dir >= SLICE * 12 - OFFSET && dir <= SLICE * 12 + OFFSET ) {
			return W;
		} else if( dir >= SLICE * 2 - OFFSET && dir <= SLICE * 2 + OFFSET ) {
			return NE;
		} else if( dir >= SLICE * 6 - OFFSET && dir <= SLICE * 6 + OFFSET ) {
			return SE;
		} else if( dir >= SLICE * 10 - OFFSET && dir <= SLICE * 10 + OFFSET ) {
			return SW;
		} else if( dir >= SLICE * 14 - OFFSET && dir <= SLICE * 14 + OFFSET ) {
			return NW;
		} else if( dir >= SLICE * 1 - OFFSET && dir <= SLICE * 1 + OFFSET ) {
			return NNE;
		} else if( dir >= SLICE * 3 - OFFSET && dir <= SLICE * 3 + OFFSET ) {
			return ENE;
		} else if( dir >= SLICE * 5 - OFFSET && dir <= SLICE * 5 + OFFSET ) {
			return ESE;
		} else if( dir >= SLICE * 7 - OFFSET && dir <= SLICE * 7 + OFFSET ) {
			return SSE;
		} else if( dir >= SLICE * 9 - OFFSET && dir <= SLICE * 9 + OFFSET ) {
			return SSW;
		} else if( dir >= SLICE * 11 - OFFSET && dir <= SLICE * 11 + OFFSET ) {
			return WSW;
		} else if( dir >= SLICE * 13 - OFFSET && dir <= SLICE * 13 + OFFSET ) {
			return WNW;
		} else if( dir >= SLICE * 15 - OFFSET && dir <= SLICE * 15 + OFFSET ) {
			return NNW;
		}

		return NON;
	}

}
