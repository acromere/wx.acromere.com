package com.acromere.wx;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherApplicationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withConfiguration( AutoConfigurations.of( WeatherApplication.class ) );

	@Test
	public void contextLoads() {
		assertThat( contextRunner ).isNotNull();
	}

}
