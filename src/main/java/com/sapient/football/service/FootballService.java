package com.sapient.football.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.football.Utils;
import com.sapient.football.exception.InvalidInputException;
import com.sapient.football.exception.ResultNotFoundException;
import com.sapient.football.model.Country;
import com.sapient.football.model.ErroResponse;
import com.sapient.football.model.FootBallResponse;
import com.sapient.football.model.League;
import com.sapient.football.model.Standing;
import com.sapient.football.model.Team;

@Service
public class FootballService {

	private static final String API_KEY_PARAM = "&APIkey=";
	public static final String API_KEY_VALUE = "9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978";
	public static final String FETCH_COUNTRIES_API_URL = "https://apiv2.apifootball.com/?action=get_countries";
	public static final String STANDING_API_URL = "https://apiv2.apifootball.com/?action=get_standings&league_id=";
	public static final String COMPETITION_API_URL = "https://apiv2.apifootball.com/?action=get_leagues&country_id=";
	private static final String TEAM_URI = "https://apiv2.apifootball.com/?action=get_teams&league_id=";

	private static final Logger LOGGER = LoggerFactory.getLogger(FootballService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public FootBallResponse getStandingDetails(String countryName, String leagueName, String teamName)
			throws JsonParseException, JsonMappingException, IOException, ResultNotFoundException, InvalidInputException {
		if(Utils.isAnyNullOrEmpty(countryName, leagueName, teamName)) {
			throw new InvalidInputException("One or more parameters are missing in request");
		}
		
		return findStandingTeamDetails(countryName, leagueName, teamName);
	}

	private FootBallResponse findStandingTeamDetails(@NonNull String countryName, @NonNull String leagueName,
			@NonNull String teamName) throws JsonParseException, JsonMappingException, IOException, ResultNotFoundException {

		Country countryDetails = getCountryDetails(countryName);

		League leagueDetails = getLeagueDetails(leagueName, countryDetails);

		Team teamDetails = getTeamDetails(teamName, leagueDetails);

		Standing standing = getStandingDetails(countryName, leagueDetails, teamDetails);

		if (standing != null) {
			FootBallResponse res = new FootBallResponse();
			res.setCountryName(countryName);
			res.setCountryId(countryDetails.getCountryId());
			res.setTeamId(teamDetails.getTeamKey());
			res.setTeamName(teamDetails.getTeamName());
			res.setOverallLeaguePosition(standing.getOverallLeaguePosition());
			return res;
		} else {
			throw new ResultNotFoundException("No matching standing details found");
		}
	}

	/**
	 * Find a standing based on countryName, leagueDetails and team details
	 * @param countryName
	 * @param leagueDetails
	 * @param teamDetails
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws ResultNotFoundException
	 */
	private Standing getStandingDetails(String countryName, League leagueDetails, Team teamDetails)
			throws IOException, JsonParseException, JsonMappingException, ResultNotFoundException {
		final String standingUri = STANDING_API_URL + leagueDetails.getLeagueId() + API_KEY_PARAM + API_KEY_VALUE;
		String jsonStr = getJsonResponse(standingUri);

		ResponseEntity<List<Standing>> standingResponse = objectMapper.readValue(jsonStr,
				new TypeReference<List<Standing>>() {
				});

		List<Standing> standingList = standingResponse.getBody();

		Standing standingDetails = standingList.stream()
				.filter(t -> teamDetails.getTeamKey().equals(t.getTeamId()))
				.filter(t -> countryName.equals(t.getCountryName()))
				.filter(t -> leagueDetails.getLeagueId().equals(t.getLeagueId())).findFirst().orElse(null);
		return standingDetails;
	}

	/**
	 * Find a team based on team and league details
	 * 
	 * @param teamName
	 * @param leagueDetails
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws ResultNotFoundException
	 */
	private Team getTeamDetails(String teamName, League leagueDetails)
			throws IOException, JsonParseException, JsonMappingException, ResultNotFoundException {
		final String teamUri = TEAM_URI + leagueDetails.getLeagueId() + API_KEY_PARAM + API_KEY_VALUE;
		String jsonStr = getJsonResponse(teamUri);

		List<Team> teamDetailsList = objectMapper.readValue(jsonStr, new TypeReference<List<Team>>() {
		});

		Team teamDetails = teamDetailsList.stream().filter(t -> teamName.equals(t.getTeamName())).findFirst()
				.orElse(null);

		if (teamDetails == null) {
			throw new ResultNotFoundException("No matching team found for team " + teamName);
		}
		return teamDetails;
	}

	/**
	 * Find a matching league based on league name and country
	 * @param leagueName
	 * @param countryDetails
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws ResultNotFoundException
	 */
	private League getLeagueDetails(@NonNull String leagueName, @NonNull Country countryDetails)
			throws IOException, JsonParseException, JsonMappingException, ResultNotFoundException {
		final String leagueApiURI = COMPETITION_API_URL + countryDetails.getCountryId() + API_KEY_PARAM + API_KEY_VALUE;
		String jsonStr = getJsonResponse(leagueApiURI);

		List<League> leagueDetailsList = objectMapper.readValue(jsonStr,
				new TypeReference<List<League>>() {
				});

		League leagueDetails = leagueDetailsList.stream()
				.filter(countryDetail -> leagueName.equals(countryDetail.getLeagueName())).findFirst().orElse(null);

		if (leagueDetails == null) {
			throw new ResultNotFoundException("No matching league found for league " + leagueName);
		}
		return leagueDetails;
	}

	/**
	 * Find a country based on country name
	 * @param countryName
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ResultNotFoundException
	 */
	@NonNull
	private Country getCountryDetails(@NonNull String countryName)
			throws JsonParseException, JsonMappingException, IOException, ResultNotFoundException {
		final String fetchCuntriesUri = FETCH_COUNTRIES_API_URL + API_KEY_PARAM + API_KEY_VALUE;

		String jsonStr = getJsonResponse(fetchCuntriesUri);

		List<Country> countryDetailsList = objectMapper.readValue(jsonStr,
				new TypeReference<List<Country>>() {
				});

		if (countryDetailsList != null && !countryDetailsList.isEmpty()) {

			Country countryDetails = countryDetailsList.stream()
					.filter(countryDetail -> countryName.equals(countryDetail.getCountryName())).findFirst()
					.orElse(null);

			if (countryDetails == null) {
				throw new ResultNotFoundException("No matching country fount for country " + countryName);
			}

			return countryDetails;
		} else {
			throw new ResultNotFoundException("No matching country fount for country " + countryName);
		}
	}

	private String getJsonResponse(@NonNull final String uri)
			throws IOException, JsonParseException, JsonMappingException, ResultNotFoundException {
		String jsonStr = restTemplate.getForObject(uri, String.class);
		handleErrorResponse(jsonStr);
		return jsonStr;
	}

	private void handleErrorResponse(String jsonStr) throws IOException, JsonParseException, JsonMappingException, ResultNotFoundException {
		ErroResponse error = null;
		try {
			error = objectMapper.readValue(jsonStr, new TypeReference<ErroResponse>() {
			});

		} catch (Exception ignore) {
		}

		if (error != null) {
			throw new ResultNotFoundException(error.getMessage());
		}
	}

}
