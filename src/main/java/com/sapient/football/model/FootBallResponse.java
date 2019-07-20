package com.sapient.football.model;

public class FootBallResponse {
	
	private String countryId;
	
	private String countryName;
	
	private String teamId;
	
	private String teamName;
	
	private String overallLeaguePosition;

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getOverallLeaguePosition() {
		return overallLeaguePosition;
	}

	public void setOverallLeaguePosition(String overallLeaguePosition) {
		this.overallLeaguePosition = overallLeaguePosition;
	}

}
