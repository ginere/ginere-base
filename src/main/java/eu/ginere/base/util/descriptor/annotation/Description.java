package eu.ginere.base.util.descriptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotation description allows to add one description of the fields.
 * It also mark the fields to be JSON exported.
 * @author ventura
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
	String description() default "";
	String profile() default "";
	
}
