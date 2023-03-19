package com.ems.api.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class DomainConfig {
    private String domain = "http://localhost";
    private String apiPort = "8081";
    private String contactEmail = "g36.ems@gmail.com";
    private String logoUrl = "hihi.com";

}
