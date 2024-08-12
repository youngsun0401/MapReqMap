package com.example.mapreqmap.config;

import com.example.mapreqmap.mapreqmap.MyRequestMappingHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer
{

	@Bean
	MyRequestMappingHandlerMapping myRequestMappingHandlerMapping () {
		return new MyRequestMappingHandlerMapping();
	}
}
