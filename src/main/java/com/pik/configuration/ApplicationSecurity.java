package com.pik.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;


@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Resource
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Resource
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception{
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/*/**").permitAll()
                .antMatchers("/login", "/api/account/**").permitAll()
                .antMatchers("/logout", "/api/**").authenticated();

        // Handlers and entry points
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.formLogin().successHandler(authenticationSuccessHandler);
        http.formLogin().failureHandler(authenticationFailureHandler);

        // Logout
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);

        // CSRF
        http.csrf().disable();
//        http.csrf().requireCsrfProtectionMatcher(
//                new AndRequestMatcher(
//                        // Apply CSRF protection to all paths that do NOT match the ones below
//
//                        // We disable CSRF at login/logout, but only for OPTIONS methods
//                        new NegatedRequestMatcher(new AntPathRequestMatcher("/login*/**", HttpMethod.OPTIONS.toString())),
//                        new NegatedRequestMatcher(new AntPathRequestMatcher("/logout*/**", HttpMethod.OPTIONS.toString())),
//
//                        new NegatedRequestMatcher(new AntPathRequestMatcher("/api*/**", HttpMethod.GET.toString())),
//                        new NegatedRequestMatcher(new AntPathRequestMatcher("/api*/**", HttpMethod.HEAD.toString())),
//                        new NegatedRequestMatcher(new AntPathRequestMatcher("/api*/**", HttpMethod.OPTIONS.toString())),
//                        new NegatedRequestMatcher(new AntPathRequestMatcher("/api*/**", HttpMethod.TRACE.toString())),
//                        new NegatedRequestMatcher(new AntPathRequestMatcher("/api/account/register", HttpMethod.POST.toString())),
//                        new NegatedRequestMatcher(new AntPathRequestMatcher("/api/open*/**"))
//                )
//        );
//        http.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class); // CSRF tokens handling
    }

}