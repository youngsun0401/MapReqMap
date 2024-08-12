package com.example.mapreqmap.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mapreqmap.mapreqmap.MyMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@MyMapping( path = "/member" , produce = MyMapping.JSON )
public class MemberController
{
	// http://localhost:8080/v1/member/get?id=123
	@MyMapping( path = "/get" , version = 1 )
	public Map< String, Object > get1 ( @RequestParam Integer id ) {

		Map< String, Object > result = new HashMap<>();
		result.put( "id", id );
		result.put( "name", "홍길동" );
		result.put( "age", 16 );

		return result;
	}

	// http://localhost:8080/v2/member/get?id=abc
	@MyMapping( path = "/get" , version = 2 )
	public Map< String, Object > get2 ( @RequestParam String id ) {

		Map< String, Object > result = new HashMap<>();
		result.put( "id", id );
		result.put( "name", "홍길동" );
		result.put( "age", 16 );
		result.put( "hobby", "수렵채집" );

		return result;
	}

	// http://localhost:8080/v3/member/all
	@MyMapping( path = "/all" , version = 3 )
	public List< Map< String, Object > > all () {

		Map< String, Object > m1 = new HashMap<>();
		m1.put( "id", "hong" );
		m1.put( "name", "홍길동" );
		m1.put( "age", 16 );
		m1.put( "hobby", "수렵채집" );

		Map< String, Object > m2 = new HashMap<>();
		m2.put( "id", "kim" );
		m2.put( "name", "김영선" );
		m2.put( "age", 543 );
		m2.put( "hobby", "피아노 연주" );

		return List.of( m1, m2 );
	}
}
