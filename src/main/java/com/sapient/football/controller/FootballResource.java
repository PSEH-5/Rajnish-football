package com.sapient.football.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.football.exception.ResultNotFoundException;
import com.sapient.football.model.FootBallResponse;
import com.sapient.football.model.ResponseWrapper;
import com.sapient.football.service.FootballService;

@RestController
public class FootballResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(FootballResource.class);
	
	@Autowired
	private FootballService footballService;

	@GetMapping("/api/findStandingDetails")
	public ResponseEntity<ResponseWrapper> getTeamStanding(@RequestParam final String countryName,
			@RequestParam final String leagueName, @RequestParam final String teamName) {
		try {
			FootBallResponse standingDetails = footballService.getStandingDetails(countryName, leagueName, teamName);
			return ResponseEntity.ok(ResponseWrapper.success(standingDetails));
		} catch (ResultNotFoundException e) {
			LOGGER.error("No result found", e);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResponseWrapper.error(e.getMessage()));
		} catch (Exception ex) {
			LOGGER.error("Error occurred", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseWrapper.error(ex.getMessage()));
		}
	}
}