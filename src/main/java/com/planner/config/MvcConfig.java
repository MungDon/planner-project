package com.planner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

	@Configuration
	public class MvcConfig implements WebMvcConfigurer {

		   @Value("${file.Upfolder}")
		    private String folder;

		    @Override
		    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		        registry.addResourceHandler("/noticeImg/**") 	//리소스 등록 및 핸들러를 관리하는 객체인 ResourceHandlerRegistry를 통해 리소스 위치와 이 리소스와 매칭될 url을 등록합니다.
		                .addResourceLocations(folder); 		 	//noticeImg로 시작하는url로 요청을 할경우
		        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/templates/", "classpath:/static/");
		    }

		}
	
