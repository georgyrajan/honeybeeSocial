package com.oracle.survey.surveyadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.oracle.survey.surveyadmin.filter.RequestResponseLoggingFilter;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * Main class for springboot config
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */

@EnableEurekaClient
@EnableSwagger2
@SpringBootApplication
public class SurveyadminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveyadminApplication.class, args);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.oracle.survey.surveyadmin")).build();
	}
	
	@LoadBalanced	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter() {
		FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(new RequestResponseLoggingFilter());
		registrationBean.addUrlPatterns("/admin/*", "/survey/*");

		return registrationBean;
	}
}
