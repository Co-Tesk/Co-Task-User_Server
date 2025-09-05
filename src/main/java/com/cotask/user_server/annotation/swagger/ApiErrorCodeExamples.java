package com.cotask.user_server.annotation.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cotask.user_server.infrastructure.exception.CoTaskExceptionCode;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface ApiErrorCodeExamples {
	CoTaskExceptionCode[] value() default {};
}
