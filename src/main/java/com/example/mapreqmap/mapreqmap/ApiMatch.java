package com.example.mapreqmap.mapreqmap;

import com.example.mapreqmap.Application;


public record ApiMatch( Integer version , String path )
{

	public String getFullPath () {
		return "/v" + version + path;
	}

	public String toString () {
		return getFullPath();
	}

	/**
	 * 같은 경로의 이전 버전 api 주소
	 */
	public ApiMatch prevVersion () {
		if( version > 0 ) return new ApiMatch( version - 1 , path );
		return null;
	}

	/**
	 * 같은 경로의 다음 버전 api 주소
	 */
	public ApiMatch nextVersion () {
		if( version < Application.version_max ) return new ApiMatch( version + 1 , path );
		return null;
	}

	public String verify () {
		if( version < 0 ) return "API의 버전이 0보다 작습니다.";
		if( version > Application.version_max ) return "API의 버전이 서버의 버전보다 큽니다.";
		return null;
	}
}
