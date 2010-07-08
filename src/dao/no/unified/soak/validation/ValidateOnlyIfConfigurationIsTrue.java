package no.unified.soak.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Angir hvilken Configuration name (key) som må være aktiv for at validering
 * skal skje for dette felt.
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateOnlyIfConfigurationIsTrue {
	/**
	 * Angir hvilken Configuration name (key) som må være aktiv for at
	 * validering skal skje for dette felt.
	 * 
	 * @return
	 */
	String value();
}
