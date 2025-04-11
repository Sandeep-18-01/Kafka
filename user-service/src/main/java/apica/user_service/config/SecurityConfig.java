// package apica.user_service.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {

// @Bean
// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
// http
// .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
// .authorizeHttpRequests(auth -> auth
// .requestMatchers(
// "/v3/api-docs/**",
// "/swagger-ui/**",
// "/swagger-ui.html",
// "/users/**")
// .permitAll()
// .anyRequest().authenticated())
// .httpBasic(); // Enable basic auth for others

// return http.build();
// }
// }
