package com.ems.api.configuration;

import com.ems.api.configuration.security.filter.JwtRequestFilter;
import com.ems.api.service.impl.MyUserDetailsServiceImpl;
import com.ems.api.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };
    private final AppProperties applicationProperties;
    @Autowired
    private MyUserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = applicationProperties.getCors();
//        if (!isEmpty(config.getAllowedOrigins())
//                || !isEmpty(config.getAllowedOrigins())) {
//            source.registerCorsConfiguration("/api/**", config);
////            source.registerCorsConfiguration("/management/**", config);
//            source.registerCorsConfiguration("/v2/api-docs", config);
//            source.registerCorsConfiguration("/v3/api-docs", config);
//            source.registerCorsConfiguration("/swagger-resources", config);
//            source.registerCorsConfiguration("/swagger-ui/**", config);
//        }
//        return new CorsFilter(source);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()//.disable()
                .csrf().disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .and()
                .authorizeRequests()
                //.antMatchers("/**").permitAll()
                .antMatchers(SWAGGER_WHITELIST).permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers(HttpMethod.GET, Constant.ControllerMapping.ALL).permitAll()
                .antMatchers(Constant.ControllerMapping.AUTHENTICATION + Constant.ControllerMapping.ALL).permitAll()
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.POST + "/search").permitAll()
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.BOOTH + "/get-all-in-event").permitAll()
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.ORDER + Constant.ControllerMapping.ALL).permitAll()
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.CONTACT + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.AGENDA + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.TEAM + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.EVENT + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.TASK + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.TASK_COMMENT + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.POST + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.SPONSOR + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.BOOTH + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.TICKET + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.EVENT_CONTACT + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.ACTIVITY_LOG + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(HttpMethod.POST, Constant.ControllerMapping.PARTICIPANT + Constant.ControllerMapping.ALL).hasAnyAuthority(Constant.Role.USER.name())
                .antMatchers(Constant.ControllerMapping.FILE + Constant.ControllerMapping.ALL).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3008",
                "http://localhost:3000",
//                "https://ems-fpt.netlify.app",
                "http://103.75.186.211:80",
                "http://103.75.186.211"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("*", "Authorization", "Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
