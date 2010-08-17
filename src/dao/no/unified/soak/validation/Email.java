package no.unified.soak.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annoteres p� set-metoder i domeneklasser for � sikre at bruker angir en
 * epostadresse.
 * 
 * @author Klaus Stafto
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

}
