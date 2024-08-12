package com.example.mapreqmap.mapreqmap;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.web.bind.annotation.Mapping;


@Target( { ElementType.METHOD, ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Mapping
@Documented
public @interface MyMapping {
	static final String JSON = "application/json; charset=UTF-8";
	static final String TEXT = "text/plain; charset=UTF-8";

	final String procude_default = TEXT;

	int version() default 0;// 메서드가 클래스 덮어씀

	String path() default "";// 클래스의 path + 메서드의 path

	String produce() default "";// 메서드가 클래스 덮어씀, 둘 다 값 없으면 procude_default
}
