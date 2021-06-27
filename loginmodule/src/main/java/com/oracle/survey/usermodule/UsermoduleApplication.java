package com.oracle.survey.usermodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

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

@ServletComponentScan
@SpringBootApplication
@EnableSwagger2
public class UsermoduleApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(UsermoduleApplication.class, args);
	}

	 @Bean
	   public Docket productApi() {
	      return new Docket(DocumentationType.SWAGGER_2).select()
	         .apis(RequestHandlerSelectors.basePackage("com.oracle.survey.usermodule")).build();
	   }

}
