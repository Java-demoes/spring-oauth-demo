package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods;

@SpringBootApplication
@EnableResourceServer // spring filter is added that protects all endpoints except /oauth/authorize.
public class OauthDemoApplication {


    public static void main(String[] args) {
		SpringApplication.run(OauthDemoApplication.class, args);
	}


    @Bean
	public ResourceServerConfigurer resourceServer(){
	    return new ResourceServerConfigurerAdapter() {
            @Autowired
            private Environment env;

            @Override
            public void configure(ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) throws Exception {
                // @formatter:off
                    resourceServerSecurityConfigurer.resourceId("Demo-resource");
                // @formatter:on
            }

            @Override
            public void configure(HttpSecurity httpSecurity) throws Exception {
                // @formatter:off
                    httpSecurity.authorizeRequests()
                            .antMatchers("/api/**").access("#oauth.hasScope('api')")
                            .antMatchers("/oauth/**").permitAll()
                            .antMatchers("/console").permitAll()
                            .antMatchers("/actuator","/actuator/**").permitAll()
                            .anyRequest().permitAll();
                // @formatter:on
            }
        };
    }
}
