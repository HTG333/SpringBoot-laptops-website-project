package com.electrxh.laptop_website.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	

	private final CustomUserDetailsService userDetServ;
	
  
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login_reg",
                                        "/assets/**",
                                        "/vendor/**",
                                        "/reg/save",
                                        "/home"
                                        , "/view_tech",
                                        "/search",
                                        "/laptop_details/*",
                                        "/api/v1/laptops/**")
                        .permitAll().anyRequest().authenticated());
        
                http.formLogin(form -> form
                        .loginPage("/login_reg")
                        .loginProcessingUrl("/login_reg")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/login_reg?error=true")
                        .permitAll());
                
                http.logout(logout -> logout
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());

        return http.build();
    }
   
    
 
    public void configure(AuthenticationManagerBuilder builder) throws Exception{
    	builder.userDetailsService(userDetServ).passwordEncoder(passwordEncoder());
    }
}