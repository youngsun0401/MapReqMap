package com.example.mapreqmap.mapreqmap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.mapreqmap.Application;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


/**
 * @see MyMapping 커스텀 어노테이션을 이용하여 api 경로를 지정합니다.
 */
public class MyRequestMappingHandlerMapping extends RequestMappingHandlerMapping
{
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( MyRequestMappingHandlerMapping.class );

	@Override
	protected void detectHandlerMethods ( Object handlerObj ) {

		Map< ApiMatch, Method > matchingHandlerMethod = new HashMap<>();

		String handlerBeanName = handlerObj.toString();
		Object apiHandler = getApplicationContext().getBean( handlerBeanName );
		Class< ? > apiHandlerClass = AopProxyUtils.ultimateTargetClass( apiHandler );// 컨트롤러에 Aspect를 적용한 경우 apiHandler가 프록시 객체이기 때문에 원래 객체의 클래스를 가져옴

		MyMapping handlerMapping = apiHandlerClass.getAnnotation( MyMapping.class );

		try{
			collectAnnotatedMethods( matchingHandlerMethod, apiHandlerClass, handlerMapping );
		}
		catch( Exception e ){
			throw new RuntimeException( "API 매핑 오류: 컨트롤러 " + handlerBeanName + " 내 " + e.getMessage() );
		}

		for( ApiMatch m : matchingHandlerMethod.keySet() ){

			List< ApiMatch > apiMatchs = new ArrayList<>();// 어노테이션에 지정된 버전부터 1씩 올리며 최대치(=서버의 API 버전) or 그 버전에 다른 메서드의 매핑이 있기 전까지의 모든 경로 모음 (예: /v1/myapi, /v2/myapi, ...)
			for( ApiMatch m1 = m ;; ){
				apiMatchs.add( m1 );

				if( m.version() == 0 )// 단; 버전 0은 테스트용이기 때문에 버전 1,2,3,...을 수집하지 않고 0에서 그침
					break;

				m1 = m1.nextVersion();
				if( m1 == null ) break;
				if( matchingHandlerMethod.keySet().contains( m1 ) ) break;
			}

			Method handlerMethod = matchingHandlerMethod.get( m );
			MyMapping methodMapping = handlerMethod.getAnnotation( MyMapping.class );

			super.registerHandlerMethod( apiHandler, handlerMethod, RequestMappingInfo.paths( apiMatchs2FullPathStrings( apiMatchs ) ).produces( getProduce( handlerMapping, methodMapping ) ).build() );

			String vs = "";
			for( ApiMatch m2 : apiMatchs ){
				vs += m2.version() + " ";
			}
			log.info( "register an api handler: " + m.getFullPath() + " -- matching versions: " + vs );
		}
	}

	private void collectAnnotatedMethods ( Map< ApiMatch, Method > matchingHandlerMethod , Class< ? > apiHandlerClass , MyMapping handlerMapping ) {

		Method[] methods = apiHandlerClass.getDeclaredMethods();
		for( Method method : methods ){
			MyMapping methodMapping = method.getAnnotation( MyMapping.class );
			if( methodMapping != null ){
				ApiMatch m = composeApiMatch( handlerMapping, methodMapping );

				String msg = verifyMethod( method, m );
				if( msg != null ){
					throw new RuntimeException( m.getFullPath() + "의 형식 무효 ... " + msg );
				}

				if( matchingHandlerMethod.get( m ) != null ){
					throw new RuntimeException( "매핑 (" + m.getFullPath() + ")은 중복입니다." );
				}

				matchingHandlerMethod.put( m, method );
			}
		}
	}

	private ApiMatch composeApiMatch ( MyMapping handlerMapping , MyMapping methodMapping ) {
		final int version;
		final String path;

		if( handlerMapping == null ){
			version = methodMapping.version();
			path = methodMapping.path();
		}
		else{
			version = methodMapping.version() != 0 ? methodMapping.version() : handlerMapping.version();
			path = handlerMapping.path() + methodMapping.path();
		}

		return new ApiMatch( version , path );
	}

	private String verifyMethod ( Method method , ApiMatch m ) {
		String msg;
		msg = veryfyMethod_version( method, m );
		if( msg != null ) return msg;

		return null;
	}

	private String veryfyMethod_version ( Method method , ApiMatch m ) {
		//// 각 API 버전은 서버의 API 버전 이하여야 합니다.

		int version_max = Application.version_max;// 서버의 API 버전

		if( m.version() > version_max ){
			return "API 버전 설정 오류: 버전 " + m.version() + "은 현재 서버의 버전인 " + version_max + "보다 큽니다.";
		}

		return null;
	}

	private String[] apiMatchs2FullPathStrings ( List< ApiMatch > apiMatches ) {
		String[] fullPaths = new String[apiMatches.size()];
		for( int i = 0 ; i < fullPaths.length ; i++ ){
			fullPaths[i] = apiMatches.get( i ).getFullPath();
		}
		return fullPaths;
	}

	private String getProduce ( MyMapping handlerMapping , MyMapping methodMapping ) {
		// 클래스의 설정값 > 메서드의 설정값 > 기본값
		if( methodMapping.produce().equals( "" ) ){
			if( handlerMapping == null || handlerMapping.produce().equals( "" ) ){
				return MyMapping.procude_default;
			}
			else{
				return handlerMapping.produce();
			}
		}
		else{
			return methodMapping.produce();
		}
	}
}
