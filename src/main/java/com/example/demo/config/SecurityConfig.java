package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		
		
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public RoleHierarchy roleHierarchy() {
		
		RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
		
		hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
		return hierarchy;
		
	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		
		// 경로에 따른 인가 작업
		/* 순서가 중요
		 * permitall -> hasrole -> anyrole
		 * */
		http
				.authorizeHttpRequests((auth) -> auth
						.requestMatchers("/", "/login", "/join", "/joinProc").permitAll()
						.requestMatchers("/admin").hasRole("ADMIN")
						.requestMatchers("/my/**").hasAnyRole("USER") // ** : wildcard, admin을 적지 않아도 hierarchy에 의해 자동포함
						.anyRequest().authenticated()
						);
		
		
		// 로그인 폼 작업
		http
				.formLogin((auth) -> auth.loginPage("/login")
						.loginProcessingUrl("/loginProc")
						.permitAll()
						);
		
		
		// http basic 인증 진행
//		http
//				.httpBasic(Customizer.withDefaults());
//		
		
		// csrf는 토큰이 있어야 토큰검정을 할수 있게 되므로 없으면 진행 안됨
//		http
//				.csrf((auth) -> auth.disable()); // 개발환경에서 꺼둠
				// 배포환경에서는 disable설정을 제거하고 추가진행
				// 디폴트는 enable
				
		
		
		
		// 다중 세션
		http
				.sessionManagement((auth) -> auth
				.maximumSessions(1) // 아이디당 하나의 세션만. 
				.maxSessionsPreventsLogin(true)); // 위의 값을 초과했을 경우 새 세션 로그인 차단 
		
		
		// 고정 보호. 로그인시 동일한 세션에 대한 id 변경
		http
				.sessionManagement((auth) -> auth
						.sessionFixation().changeSessionId());
		
		
		// 로그아웃
		http
		        .logout((auth) -> auth.logoutUrl("/logout")
		                .logoutSuccessUrl("/"));

		
		
		return http.build();
		/* 빌더타입 리턴 이유
		 * 특정사용자 요청 경로 만들기
		 */
	}
}
