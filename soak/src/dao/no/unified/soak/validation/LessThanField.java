package no.unified.soak.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annoteres på set-metoder i domeneklasser for å sikre at bruker angir verdi i
 * feltet.
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LessThanField {
	String value();
}
