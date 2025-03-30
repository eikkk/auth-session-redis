package com.plainprog.auth_session_redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class })
public class AuthSessionRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthSessionRedisApplication.class, args);
	}

}
