package com.sapient.football.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coach {
	
	@JsonProperty("coach_name")
	private String coachName;
	
	@JsonProperty("coach_country")
	private String coachCountry;
	
	@JsonProperty("country_age")
	private String coachAge;

	public String getCoachName() {
		return coachName;
	}

	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}

	public String getCoachCountry() {
		return coachCountry;
	}

	public void setCoachCountry(String coachCountry) {
		this.coachCountry = coachCountry;
	}

	public String getCoachAge() {
		return coachAge;
	}

	public void setCoachAge(String coachAge) {
		this.coachAge = coachAge;
	}

}
