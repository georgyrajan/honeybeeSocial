package com.oracle.survey.surveycustomer;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
/**
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SurveycustomerApplication.class);
	}

}
