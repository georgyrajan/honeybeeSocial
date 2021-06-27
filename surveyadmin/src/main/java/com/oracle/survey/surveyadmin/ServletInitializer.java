package com.oracle.survey.surveyadmin;
/**
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SurveyadminApplication.class);
	}

}
