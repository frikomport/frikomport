package no.unified.soak.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annoteres p� set-metoder i domeneklasser for � sikre at bruker angir verdi
 * med tilstrekkelig antall tegn i feltet.
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxLength {
	/**
	 * Angir maksimum antall tegn som kreves. Hvis null, settes ingen begrensning
	 * p� antall tegn.
	 * 
	 * @return
	 */
	String value();
}
