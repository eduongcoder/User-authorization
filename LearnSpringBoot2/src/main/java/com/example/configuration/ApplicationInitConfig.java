package com.example.configuration;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.entity.User;
import com.example.enums.Role;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	@ConditionalOnProperty(prefix = "spring",
	value = "datasource.driver-Class-Name",havingValue = "com.mysql.cj.jdbc.Driver")
	ApplicationRunner applicationRunner(UserRepository userRepository) { 
		log.info("Init application..........");
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				
				var roles = new HashSet<String>();
				roles.add(Role.ADMIN.name());
				User user = User.builder().username("admin").password(passwordEncoder.encode("admin"))
//						.roles(roles)
						.build();
				userRepository.save(user);
				log.warn("Admin user has been created with default password: admin, please change it");
			} 
		};
	}
}
