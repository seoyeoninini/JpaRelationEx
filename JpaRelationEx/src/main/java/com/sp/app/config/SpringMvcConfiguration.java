package com.sp.app.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sp.app.interceptor.LoginCheckInterceptor;

@Configuration
public class SpringMvcConfiguration implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> excludePaths = new ArrayList<>();
		excludePaths.add("/");
		excludePaths.add("/dist/**");
		excludePaths.add("/member/login");
		excludePaths.add("/member/logout");
		excludePaths.add("/member/member");
		excludePaths.add("/member/complete");
		
		registry.addInterceptor(new LoginCheckInterceptor()).excludePathPatterns(excludePaths);
	}

}
