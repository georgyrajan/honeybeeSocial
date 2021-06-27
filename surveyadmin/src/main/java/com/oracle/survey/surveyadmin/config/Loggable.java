package com.oracle.survey.surveyadmin.config;

/**
 * This is custom annotation to log matrix
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {

}
