package com.Soganis.Config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow both HTTP and HTTPS by using wildcards or specific patterns
        config.setAllowedOriginPatterns(List.of("http://*", "https://*")); // Wildcards for HTTP and HTTPS origins

        // Specify allowed methods
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");

        // Allow all headers (customize as needed)
        config.addAllowedHeader("*");

        // Enable credentials if needed
        config.setAllowCredentials(true);

        // Register the configuration for all endpoints
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
