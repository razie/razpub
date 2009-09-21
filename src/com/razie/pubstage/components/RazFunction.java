package com.razie.pubstage.components;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * a function of a component - like a method call or the main functionality or...
 * 
 * the idea is that this is a point that can be hooked up in a logical diagram and results sent
 * elsewhere...
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
@Inherited
public @interface RazFunction {
    String name();

    String descr();

    String[] inputs() default {};// parms and optional types, see AA

    String[] outputs() default {};// parms and optional types, see AA
}
