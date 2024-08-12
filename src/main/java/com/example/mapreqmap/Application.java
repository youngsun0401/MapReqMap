package com.example.mapreqmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application
{

	public static final int version_max = 3;

	public static void main ( String[] args ) {
		SpringApplication.run( Application.class, args );
	}

}
