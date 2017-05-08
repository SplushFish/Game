package net.dragonclaw.sqlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLField {
    String dataType() default "INTEGER";

    boolean notNull() default false;

    boolean primaryKey() default false;

    boolean unique() default false;

    boolean autoIncrement() default false;
    
    boolean newField() default false;
}
