package com.planner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.planner.util.FriendRoleUtils;

@Configuration
public class StatusConfig {
	
	@Bean
	protected FriendRoleUtils friendRoleUtils() {
		return new FriendRoleUtils();
	}
}
