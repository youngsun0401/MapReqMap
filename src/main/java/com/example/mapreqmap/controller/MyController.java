package com.example.mapreqmap.controller;

import com.example.mapreqmap.mapreqmap.MyMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MyController
{
	//// http://localhost:8080/v0
	@MyMapping( path = "" , version = 0 , produce = MyMapping.TEXT )
	public String who () {
		return "map req map!";
	}

	//// http://localhost:8080/v1/hello
	@MyMapping( path = "/hello" , version = 1 , produce = MyMapping.TEXT )
	public String hello () {
		return "hello world!";
	}
}
