package com.sapient.football.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Team {
	
	@JsonProperty("team_key")
	private String teamKey;

	@JsonProperty("team_name")
	private String teamName;

	@JsonProperty("team_badge")
	private String teamBadge;

	private List<Player> players;
	
	private List<Coach> coaches;

	public String getTeamKey() {
		return teamKey;
	}

	public void setTeamKey(String teamKey) {
		this.teamKey = teamKey;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamBadge() {
		return teamBadge;
	}

	public void setTeamBadge(String teamBadge) {
		this.teamBadge = teamBadge;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<Coach> getCoaches() {
		return coaches;
	}

	public void setCoaches(List<Coach> coaches) {
		this.coaches = coaches;
	}
}
