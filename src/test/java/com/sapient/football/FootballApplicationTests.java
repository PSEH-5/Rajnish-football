package com.sapient.football;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sapient.football.exception.InvalidInputException;
import com.sapient.football.service.FootballService;

@SpringBootTest
class FootballApplicationTests {

	@Autowired
	private FootballService footballService;

	@Test
	void null_all_params() throws Exception {
		assertThrows(InvalidInputException.class, () -> {
			footballService.getStandingDetails(null, null, null);
		});
	}

	@Test
	void null_country_name() throws Exception {
		assertThrows(InvalidInputException.class, () -> {
			footballService.getStandingDetails(null, "test", "test");
		});
	}

}
