package com.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private EmployeeDetailsService employeeDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/employees/organization/**").permitAll()
                .antMatchers("/employees/superAdmin").permitAll()
                .antMatchers("/employees/").hasAnyRole("ADMIN","SUPER_ADMIN")
                .antMatchers("/employees/role/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                .antMatchers(HttpMethod.DELETE,"/employees/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                .antMatchers(HttpMethod.POST,"/assets/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                .antMatchers(HttpMethod.PUT,"/assets/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                .antMatchers(HttpMethod.DELETE,"/assets/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                .anyRequest().authenticated().and().httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(employeeDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
