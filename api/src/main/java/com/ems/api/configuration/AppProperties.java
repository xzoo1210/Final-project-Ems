package com.ems.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:application.properties")
public class AppProperties {

    private static Environment environment;
    private final CorsConfiguration cors = new CorsConfiguration();

    @Autowired
    public AppProperties(Environment environment) {
        AppProperties.environment = environment;
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public static <T> T getProperty(String key, Class<T> type) {
        return environment.getProperty(key, type);
    }

    public CorsConfiguration getCors() {
        return cors;
    }
}
