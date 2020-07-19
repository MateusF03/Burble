package com.mateus.burble.command.annotations;

import com.mateus.burble.argument.ArgumentType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Arguments.class)
public @interface ArgumentAnnotation {
    String name();
    ArgumentType type();
    boolean obligatory();
}
