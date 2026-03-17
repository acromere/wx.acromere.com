package com.acromere.wx;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FlightCondition {

	public enum Summary {
		GREAT,
		GOOD,
		FAIR,
		POOR,
		HOLD
	}

	public enum Reason {
		HOT,
		WARM,
		COOL,
		COLD,
		BREEZY,
		WINDY,
		BUMPY,
		GUSTY,
		RAINY,
		DAWN,
		DUSK,
		DARK
	}

	private Summary summary = Summary.GREAT;

	private List<Reason> reasons = new ArrayList<>();

	public void reset() {
		summary = Summary.GREAT;
		reasons = new ArrayList<>();
	}

}
