package ru.yakunina.filmrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import ru.yakunina.filmrest.filters.AuthFilter;

@SpringBootApplication
public class FilmrestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmrestApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");  // сюда можно вписать http://localhost:8080
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		registrationBean.setFilter(new CorsFilter(source));
		registrationBean.setOrder(0);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterFilterRegistrationBean(){
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/usr/*");            // сюда доступ с токен
		registrationBean.addUrlPatterns("/cinemasession/*");  // сюда доступ с токен
		registrationBean.addUrlPatterns("/film/*");  // сюда доступ с токен
		registrationBean.addUrlPatterns("/cinemahall/*");  // сюда доступ с токен
		registrationBean.addUrlPatterns("/ticket/*");  // сюда доступ с токен
		return registrationBean;
	}

}
